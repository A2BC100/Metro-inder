package com.example.metroinder.deepLearning.service;

import com.example.metroinder.dataSet.model.TimeStationPersonnel;
import com.example.metroinder.dataSet.repository.TimeStationPersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeepLearningService {
    private final TimeStationPersonnelRepository timeStationPersonnelRepository;

    public List<TimeStationPersonnel> findAllByOrderByRecordDateDesc() {
        return timeStationPersonnelRepository.findAllByOrderByRecordDateDesc();
    }


    public void trainModel() {
        try {
            // 하이퍼파라미터 설정
            int batchSize = 32; // 배치 크기 32설정, 복잡한 데이터이기 때문에 배치가 낮을 수록 좋으나 속도 이슈로 일단 32설정, 배치 1
            int numInputs = 18; // 시간대별 데이터 크기
            int numOutputs = 1; // 예제에서는 출력을 사용하지 않음
            int lstmLayerSize = 50; // LSTM 레이어의 크기
            int numEpochs = 10; // 에폭

            List<TimeStationPersonnel> dataList = findAllByOrderByRecordDateDesc();
            if (dataList == null || dataList.isEmpty()) {
                log.error("데이터를 가져오지 못했습니다.");
                return;
            }
            log.info("데이터 로드 성공");

            // 각 역, 호선, 일 별로 데이터 그룹화
            Map<String, Map<String, Map<String, List<TimeStationPersonnel>>>> groupedData = groupDataByStationDayLine(dataList);
            log.info("데이터 그룹화 성공");
            // 데이터셋 생성
            List<DataSet> dataSetList = createDataSet(groupedData);
            log.info("데이터셋 생성 성공");

            // 데이터셋 이터레이터 생성
            DataSetIterator iterator = new ListDataSetIterator<>(dataSetList, batchSize);

            log.info("신경망 구성 설정 시작...");
            // 신경망 구성 설정
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(123)// 결과를 위해 랜덤 시드 설정
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)// 최적화 알고리즘 선택, 현재 확률적 경사 하강법
                    .weightInit(WeightInit.XAVIER)// 가중치 초기화 방법, Xavier 초기화
                    .updater(new Adam())// 가중치 업데이트 방법, Adam 옵티마이저
                    .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)// 기울기 정규화 방법, 절대값 기반의 원소별 기울기 클램핑
                    .gradientNormalizationThreshold(0.5)// 기울기 클램핑의 임계값을 설정, 절대값이 임계값을 초과하는 경우 기울기가 잘림
                    .list()// 다층 신경망 레이어 목록 시작
                    .layer(new LSTM.Builder() // 첫 번째 레이어 추가, LSTM 레이어
                            .nIn(numInputs) // 입력 수 설정
                            .nOut(lstmLayerSize) // 출력 수 설정
                            .activation(Activation.TANH) // 활성화 함수로 하이퍼볼릭 탄젠트 함수 사용
                            .build())
                    /*.layer(new LSTM.Builder() // 첫 번째 레이어 추가, LSTM 레이어
                            .nIn(numInputs) // 입력 수 설정
                            .nOut(lstmLayerSize) // 출력 수 설정
                            .activation(Activation.TANH) // 활성화 함수로 하이퍼볼릭 탄젠트 함수 사용
                            .build())*/
                    .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)// 두 번째 레이어 추가, RNN 출력 레이어
                            .activation(Activation.IDENTITY)// 활성화 함수로 항등 사용
                            .nIn(lstmLayerSize)// 입력 수 설정
                            .nOut(numOutputs)// 출력 수 설정
                            .build())
                    .build();
            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            log.info("신경망 구성 설정 완료");

            log.info("데이터 스케일링 시작...");
            // 데이터 스케일링
            NormalizerMinMaxScaler scaler = new NormalizerMinMaxScaler();
            scaler.fit(iterator);
            iterator.setPreProcessor(scaler);
            log.info("데이터 스케일링 완료");

            log.info("모델 학습 시작...");
            // 신경망 초기화 및 학습
            net.init();
            net.fit(iterator, numEpochs);
            log.info("모델 학습 완료!");

            //evaluateModel(iterator, numOutputs, net);

            RegressionEvaluation eval = net.evaluateRegression(iterator);
            log.info(eval.stats());

            log.info("모델 저장 시작...");
            File modelFile = new File("MTDMProvider.zip");
            ModelSerializer.writeModel(net, modelFile, true);
            log.info("모델 저장 완료!");
        } catch (IOException e) {
            log.error("학습 중 파일 작성 오류", e);
        } catch (DataAccessException e) {
            log.error("데이터 엑세스 중 오류 발생, 데이터베이스 연결이 실패했습니다.", e);
        } catch (Exception e) {
            log.error("학습 중 에러 발생", e);
        }
    }

    public double[] getDataArray(TimeStationPersonnel data) {
        return new double[] {
                data.getSix(), data.getSeven(), data.getEight(), data.getNine(), data.getTen(),
                data.getEleven(), data.getTwelve(), data.getThirteen(), data.getFourteen(),
                data.getFifteen(), data.getSixteen(), data.getSeventeen(), data.getEighteen(),
                data.getNineteen(), data.getTwenty(), data.getTwentyOne(), data.getTwentyTwo(), data.getFromTwentyThreeToSixHour()
        };

    }

    public Map<String, Map<String, Map<String, List<TimeStationPersonnel>>>> groupDataByStationDayLine(List<TimeStationPersonnel> dataList) {
        Map<String, Map<String, Map<String, List<TimeStationPersonnel>>>> groupedData = new HashMap<>();

        for (TimeStationPersonnel data : dataList) {
            String recordDate = data.getRecordDate();
            String station = data.getStation();
            String line = data.getLine();

            groupedData.putIfAbsent(recordDate, new HashMap<>());
            groupedData.get(recordDate).putIfAbsent(station, new HashMap<>());
            groupedData.get(recordDate).get(station).putIfAbsent(line, new ArrayList<>());
            groupedData.get(recordDate).get(station).get(line).add(data);
        }

        return groupedData;
    }

    // 데이터셋 생성
    public List<DataSet> createDataSet(Map<String, Map<String, Map<String, List<TimeStationPersonnel>>>> groupedData) {
        List<DataSet> dataSetList = new ArrayList<>();

        groupedData.values().parallelStream().forEach(dayEntry -> {
            dayEntry.values().parallelStream().forEach(stationEntry -> {
                stationEntry.values().parallelStream().forEach(lineData -> {
                    double[][] inputArray = new double[lineData.size()][18];
                    double[][] outputArray = new double[lineData.size()][18];

                    IntStream.range(0, lineData.size()).parallel().forEach(i -> {
                        TimeStationPersonnel data = lineData.get(i);
                        log.info("데이터셋에 " + data.getRecordDate() + " "+ data.getStation() + " " + data.getLine() + "호선 데이터 삽입 중");

                        // 입력 데이터 생성
                        inputArray[i] = getDataArray(data);

                        // 다음 날짜의 출력 데이터 조회
                        LocalDate date = LocalDate.parse(data.getRecordDate());
                        LocalDate nextDate = date.plusDays(1);
                        String nextDateString = nextDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        TimeStationPersonnel output = timeStationPersonnelRepository.findLineAndStationAndRecordDate(data.getLine(), data.getStation(), nextDateString);

                        // 출력 데이터 생성
                        if(output == null) {
                            log.info(data.getRecordDate() + " "+ data.getStation() + " " + data.getLine() + "호선 다음일 데이터 없음");
                            outputArray[i] = new double[18];
                            Arrays.fill(outputArray[i], 0.0);
                        } else {
                            outputArray[i] = getDataArray(output);
                        }
                    });

                    // INDArray로 변환하여 데이터셋 생성
                    INDArray input = Nd4j.create(inputArray);
                    INDArray output = Nd4j.create(outputArray);

                    dataSetList.add(new DataSet(input, output));
                });
            });
        });

        return dataSetList;
    }

    public void predict(String targetLine, String targetStation, String targetDate) {
        // 저장된 모델 로드
        try {
            File modelFile = new File("MTDMProvider.zip");
            if (!modelFile.exists()) {
                log.error("모델 파일이 존재하지 않습니다.");
                return;
            }

            MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(modelFile);
            log.info("모델 로드 완료: {}", modelFile.getAbsolutePath());

            TimeStationPersonnel previousData = timeStationPersonnelRepository.findLineAndStationAndRecordDate(targetLine, targetStation, targetDate);
            if (previousData == null) {
                log.error("이전 데이터를 찾을 수 없습니다.");
                return;
            }

            // 입력 데이터 생성
            double[] inputArray = getDataArray(previousData);
            INDArray input = Nd4j.create(inputArray);

            // 모델을 사용하여 다음 시간대의 데이터 예측
            INDArray output = net.output(input);

            log.info("다음 시간대의 예측된 승하차 인원: {}", output);
        } catch (Exception e) {
            log.error("모델 로드 중 오류 발생", e);
        }
    }
    public void evaluateModel(DataSetIterator iterator, int numOutputs, MultiLayerNetwork net) {
        try {
            log.info("모델 성능 평가 시작...");

            // 모델의 예측 정확도 계산
            Evaluation evaluation = new Evaluation(numOutputs);
            while (iterator.hasNext()) {
                DataSet testData = iterator.next();
                INDArray features = testData.getFeatures();
                INDArray labels = testData.getLabels();
                INDArray predicted = net.output(features, false);
                evaluation.eval(labels, predicted);
            }

            // 평가 지표 출력
            log.info("평가 지표:");
            log.info("정확도: {}", evaluation.accuracy());
            log.info("정밀도: {}", evaluation.precision());
            log.info("재현율: {}", evaluation.recall());
            log.info("F1 점수: {}", evaluation.f1());

            log.info("모델 성능 평가 완료");
        } catch (Exception e) {
            log.error("모델 성능 평가 중 오류 발생", e);
        }
    }

    /*private double[] getDataArray(TimeStationPersonnel data) {
        // 해당 객체에서 데이터 추출하여 배열로 반환
        double[] dataArray = {
                data.getSix(), data.getSeven(), data.getEight(), data.getNine(), data.getTen(),
                data.getEleven(), data.getTwelve(), data.getThirteen(), data.getFourteen(),
                data.getFifteen(), data.getSixteen(), data.getSeventeen(), data.getEighteen(),
                data.getNineteen(), data.getTwenty(), data.getTwentyOne(), data.getTwentyTwo(), data.getFromTwentyThreeToSixHour(),
                (double) data.getRecordDate().hashCode(), (double) data.getStation().hashCode(), (double) data.getLine().hashCode()
        };
        return dataArray;
    }*/
    /*public DataSet createDataSet(List<TimeStationPersonnel> timeStationPersonnelList) {
        List<double[]> inputDataList = new ArrayList<>();
        List<double[]> outputDataList = new ArrayList<>();

        for (TimeStationPersonnel data : timeStationPersonnelList) {
            double[] inputData = getDataArray(data);
            inputDataList.add(inputData);

            String nowDate = data.getRecordDate();
            LocalDate date = LocalDate.parse(nowDate);
            LocalDate nextDate = date.plusDays(1);
            String nextDateString = nextDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            TimeStationPersonnel output = timeStationPersonnelRepository.findLineAndStationAndRecordDate(data.getLine(), data.getStation(), nextDateString);
            double[] outputData;
            if(output == null) {
                log.info(data.getRecordDate() + " "+ data.getStation() + " " + data.getLine() + "호선 다음일 데이터 없음");
                outputData = new double[21];
                Arrays.fill(outputData, 0.0);
            } else {
                outputData = getDataArray(output);
            }

            outputDataList.add(outputData);
        }

        INDArray input = Nd4j.create(inputDataList.toArray(new double[0][]));
        INDArray output = Nd4j.create(outputDataList.toArray(new double[0][]));

        DataSet dataSet = new DataSet(input, output);

        return dataSet;
    }*/
}

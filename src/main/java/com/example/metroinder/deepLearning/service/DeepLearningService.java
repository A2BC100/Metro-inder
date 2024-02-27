package com.example.metroinder.deepLearning.service;

import com.example.metroinder.dataSet.model.StationTraffic;
import com.example.metroinder.dataSet.repository.StationTrafficRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeepLearningService {
    private final StationTrafficRepository stationTrafficRepository;

    public void trainModel() {
        try {
            System.setProperty("org.nd4j.backend", "cuda");

            int batchSize = 1; // 배치 크기 32설정, 복잡한 데이터이기 때문에 배치가 낮을 수록 좋음. 일단 1로 설정.
            int numInputs = 18; // 데이터 특성 개수
            int numOutputs = 18; // 출력
            int lstmLayerSize = 50; // LSTM 레이어의 크기
            double learningRate = 0.001;
            int numEpochs = 10; // 에폭

            List<StationTraffic> dataList = stationTrafficRepository.getTrainningData();
            if (dataList == null || dataList.isEmpty()) {
                log.error("데이터를 가져오지 못했습니다.");
                return;
            }
            log.info("데이터 로드 성공");

            // 각 역, 호선, 일 별로 데이터 그룹화
            Map<String, Map<Integer, List<StationTraffic>>> groupedData = groupedData(dataList);

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
                    .updater(new Adam(learningRate))// 가중치 업데이트 방법, Adam 옵티마이저
                    .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)// 기울기 정규화 방법, 절대값 기반의 원소별 기울기 클램핑
                    .gradientNormalizationThreshold(0.5)// 기울기 클램핑의 임계값을 설정, 절대값이 임계값을 초과하는 경우 기울기가 잘림
                    .list()// 다층 신경망 레이어 목록 시작
                    .layer(new LSTM.Builder() // 첫 번째 레이어 추가, LSTM 레이어
                            .nIn(numInputs) // 입력 수 설정
                            .nOut(lstmLayerSize) // 출력 수 설정
                            .activation(Activation.TANH) // 활성화 함수로 하이퍼볼릭 탄젠트 함수 사용
                            .build())
                    .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)// 두 번째 레이어 추가, RNN 출력 레이어
                            .activation(Activation.IDENTITY)// 활성화 함수로 항등 사용
                            .nIn(lstmLayerSize)// 입력 수 설정
                            .nOut(numOutputs)// 출력 수 설정
                            .build())
                    .build();
            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            log.info("신경망 구성 설정 완료");

            // 데이터 스케일링 현재 StandardScaler
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(iterator);
            iterator.setPreProcessor(normalizer);

            log.info("데이터 스케일링 완료");

            log.info("모델 학습 시작...");
            // 신경망 초기화 및 학습
            net.init();
            net.setListeners(new ScoreIterationListener(1));// 손실율 확인
            net.fit(iterator, numEpochs);
            log.info("모델 학습 완료!");

            evaluateModel(net, batchSize, numInputs);

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

    public double[] getDataArray(StationTraffic data) {
        double[] dataArray = new double[18];
        dataArray[0] = data.getSix();
        dataArray[1] = data.getSeven();
        dataArray[2] = data.getEight();
        dataArray[3] = data.getNine();
        dataArray[4] = data.getTen();
        dataArray[5] = data.getEleven();
        dataArray[6] = data.getTwelve();
        dataArray[7] = data.getThirteen();
        dataArray[8] = data.getFourteen();
        dataArray[9] = data.getFifteen();
        dataArray[10] = data.getSixteen();
        dataArray[11] = data.getSeventeen();
        dataArray[12] = data.getEighteen();
        dataArray[13] = data.getNineteen();
        dataArray[14] = data.getTwenty();
        dataArray[15] = data.getTwentyone();
        dataArray[16] = data.getTwentytwo();
        dataArray[17] = data.getFromTwentythreeToSixHour();
        return dataArray;

        /*return new double[] {
                data.getSix(), data.getSeven(), data.getEight(), data.getNine(), data.getTen(),
                data.getEleven(), data.getTwelve(), data.getThirteen(), data.getFourteen(),
                data.getFifteen(), data.getSixteen(), data.getSeventeen(), data.getEighteen(),
                data.getNineteen(), data.getTwenty(), data.getTwentyone(), data.getTwentytwo(), data.getFromTwentythreeToSixHour()
        };*/
    }

    public Map<String, Map<Integer, List<StationTraffic>>> groupedData(List<StationTraffic> stationTrafficList) {
        Map<String, Map<Integer, List<StationTraffic>>> groupedData = new ConcurrentHashMap<>();

        for (StationTraffic traffic : stationTrafficList) {
            String recordDate = traffic.getRecordDate();
            int stationNumber = traffic.getStationNumber();

            groupedData.computeIfAbsent(recordDate, k -> new ConcurrentHashMap<>())
                    .computeIfAbsent(stationNumber, k -> new ArrayList<>())
                    .add(traffic);
        }

        return groupedData;
    }

    // 데이터셋 생성
    public List<DataSet> createDataSet(Map<String, Map<Integer, List<StationTraffic>>> groupedData) {
        List<DataSet> dataSetList = new ArrayList<>();

        groupedData.entrySet().parallelStream().forEach(dayEntry ->
                dayEntry.getValue().entrySet().parallelStream().forEach(stationEntry -> {
                    List<StationTraffic> stationDataList = stationEntry.getValue();
                    int dataSize = stationDataList.size();
                    int numFeatures = 18;
                    int numOutputs = 18;
                    int timeSteps = 1; // 각 데이터 포인트는 1 시간 간격으로 추출되므로

                    INDArray inputArray = Nd4j.create(dataSize, numFeatures, timeSteps);
                    INDArray outputArray = Nd4j.create(dataSize, numOutputs, timeSteps);

                    IntStream.range(0, dataSize).parallel().forEach(i -> {
                        StationTraffic data = stationDataList.get(i);

                        inputArray.put(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(0)},
                                Nd4j.create(getDataArray(data)));

                        LocalDate date = LocalDate.parse(dayEntry.getKey()).plusDays(1);
                        String nextDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        StationTraffic output = stationTrafficRepository.findStationAndRecordDate(data.getStationNumber(), nextDate);

                        if (output == null) {
                            /*dataNonList.add(data.getRecordDate() + " " + data.getStationNumber()); // 다음일 데이터가 없는 데이터 확인을 위해 임시 삽입*/
                            outputArray.put(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(0)},
                                    Nd4j.zeros(1, numOutputs, timeSteps)); // 0으로 채워진 INDArray를 추가
                        } else {
                            outputArray.put(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(0)},
                                    Nd4j.create(getDataArray(output)));

                        }
                    });

                    dataSetList.add(new DataSet(inputArray, outputArray));
                })
        );

        // 다음일 데이터가 없는 데이터 확인용 로그
        /*for (String noneData : dataNonList) {
            log.info(noneData);
        }*/

        return dataSetList;
    }

    public void predict(int stationNumber, String date) {
        // 저장된 모델 로드
        try {
            File modelFile = new File("MTDMProvider.zip");
            if (!modelFile.exists()) {
                log.error("모델 파일이 존재하지 않습니다.");
                return;
            }

            MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(modelFile);
            log.info("모델 로드 완료: {}", modelFile.getAbsolutePath());

            StationTraffic previousData = stationTrafficRepository.findStationAndRecordDate(stationNumber, date);
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
    public void evaluateModel(MultiLayerNetwork net, int batchSize, int numInputs) {
        List<StationTraffic> testData = stationTrafficRepository.getTestingData();

        if (testData == null || testData.isEmpty()) {
            log.error("테스트 데이터를 가져오지 못했습니다.");
            return;
        }

        try {
            // 각 역, 호선, 일 별로 데이터 그룹화
            Map<String, Map<Integer, List<StationTraffic>>> groupedData = groupedData(testData);

            // 데이터셋 생성
            List<DataSet> dataSetList = createDataSet(groupedData);
            DataSetIterator iterator = new ListDataSetIterator<>(dataSetList, batchSize);

            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(iterator);
            iterator.setPreProcessor(normalizer);

            RegressionEvaluation evaluation = new RegressionEvaluation();

            while (iterator.hasNext()) {
                DataSet next = iterator.next();
                INDArray output = net.output(next.getFeatures());
                evaluation.eval(next.getLabels(), output);
            }

            log.info("전체 평가 결과:");
            log.info("평균 제곱 오차 (MSE): {}", evaluation.meanSquaredError(0)); // 작을수록 모델 성능 좋음
            log.info("평균 절대 오차 (MAE): {}", evaluation.meanAbsoluteError(0)); // 작을수록 모델 성능 좋은
            log.info("결정계수 (R2): {}", evaluation.rSquared(0)); // 1에 가까울수록 모델 성능 좋음

        } catch (Exception e) {
            log.error("모델 평가 중 오류 발생", e);
        }
    }

    public void configureHyperparameters() {
        // 하이퍼파라미터 범위 정의
        int[] batchSizes = {1, 8, 16, 32};
        double[] learningRates = {0.001, 0.01, 0.1};
        int[] lstmLayerSizes = {50, 100, 200};
        int[] numEpochs = {5, 10, 20};

        List<StationTraffic> dataList = stationTrafficRepository.getTrainningData();
        if (dataList == null || dataList.isEmpty()) {
            log.error("데이터를 가져오지 못했습니다.");
            return;
        }
        log.info("데이터 로드 성공");

        // 각 역, 호선, 일 별로 데이터 그룹화
        Map<String, Map<Integer, List<StationTraffic>>> groupedData = groupedData(dataList);

        // 데이터셋 생성
        List<DataSet> dataSetList = createDataSet(groupedData);
        log.info("데이터셋 생성 성공");

        // 그리드 서치 수행
        for(int batchSize : batchSizes) {
            for (double learningRate : learningRates) {
                for (int lstmLayerSize : lstmLayerSizes) {
                    for (int epoch : numEpochs) {
                        trainModelWithHyperparameters(dataSetList, batchSize, learningRate, lstmLayerSize, epoch);
                    }
                }
            }
        }
    }
    public void trainModelWithHyperparameters(List<DataSet> dataSetList, int batchSize, double learningRate, int lstmLayerSize, int numEpochs) {
        // 하이퍼파라미터 설정
        int numInputs = 18; // 데이터 특성 개수
        int numOutputs = 18; // 출력

        try {
            // 데이터셋 이터레이터 생성
            DataSetIterator iterator = new ListDataSetIterator<>(dataSetList, batchSize);

            log.info("신경망 구성 설정 시작...");
            // 신경망 구성 설정
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(123)// 결과를 위해 랜덤 시드 설정
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)// 최적화 알고리즘 선택, 현재 확률적 경사 하강법
                    .weightInit(WeightInit.XAVIER)// 가중치 초기화 방법, Xavier 초기화, .weightInit(WeightInit.RELU)- He 초기화
                    .updater(new Adam(learningRate))// 가중치 업데이트 방법, Adam 옵티마이저
                    .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)// 기울기 정규화 방법, 절대값 기반의 원소별 기울기 클램핑
                    .gradientNormalizationThreshold(0.5)// 기울기 클램핑의 임계값을 설정, 절대값이 임계값을 초과하는 경우 기울기가 잘림
                    .list()// 다층 신경망 레이어 목록 시작
                    .layer(new LSTM.Builder() // 첫 번째 레이어 추가, LSTM 레이어
                            .nIn(numInputs) // 입력 수 설정
                            .nOut(lstmLayerSize) // 출력 수 설정
                            .activation(Activation.TANH) // 활성화 함수로 하이퍼볼릭 탄젠트 함수 사용
                            .build())
                    .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)// 두 번째 레이어 추가, RNN 출력 레이어, 평균 제곱 오차 손실 함수
                            .activation(Activation.IDENTITY)// 활성화 함수로 항등 사용
                            .nIn(lstmLayerSize)// 입력 수 설정
                            .nOut(numOutputs)// 출력 수 설정
                            .build())
                    .build();
            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            log.info("신경망 구성 설정 완료");

            // 데이터 스케일링 현재 StandardScaler
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(iterator);
            iterator.setPreProcessor(normalizer);
            // MinMaxScaler
            /*NormalizerMinMaxScaler scaler = new NormalizerMinMaxScaler();
            scaler.fit(iterator);
            iterator.setPreProcessor(scaler);*/
            log.info("데이터 스케일링 완료");

            log.info("모델 학습 시작...");
            // 신경망 초기화 및 학습
            net.init();
            //net.setListeners(new ScoreIterationListener(1));// 손실율 확인
            net.fit(iterator, numEpochs);
            log.info("모델 학습 완료!");

            evaluateModel(net, batchSize, numInputs);
            log.info("batchSize : " + batchSize + "learningRate : " + learningRate + ", lstmLayerSize" + ", lstmLayerSize" + ", numEpochs" + numEpochs);
        }catch (DataAccessException e) {
            log.error("데이터 엑세스 중 오류 발생, 데이터베이스 연결이 실패했습니다.", e);
        } catch (Exception e) {
            log.error("학습 중 에러 발생", e);
        }
    }
}
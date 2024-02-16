package com.example.metroinder.deepLearning.service;

import com.example.metroinder.dataSet.model.TimeStationPersonnel;
import com.example.metroinder.dataSet.repository.TimeStationPersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxScoreIterationTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
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
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeepLearningService {
    private final TimeStationPersonnelRepository timeStationPersonnelRepository;

    public List<TimeStationPersonnel> findAllByOrderByRecordDateDesc() {
        return timeStationPersonnelRepository.findAllByOrderByRecordDateDesc();
    }

    // 모델 학습 및 파일 저장
    public void trainModel() {
        try {
            // 하이퍼파라미터 설정
            int batchSize = 32; // 배치, 현재 32, 한계는 64 정도로 추정
            int numInputs = 1; // 입력 변수 수 (하나의 시계열 데이터를 사용하기 때문에 1)
            int numOutputs = 1; // 출력 변수 수 (다음 시간대의 승하차 인원 예측을 위해 1)
            int lstmLayerSize = 50; // LSTM 레이어의 크기
            int numEpochs = 10; // 에폭, 학습을 하면서 적당한 크기로 조절이 필요.

            List<TimeStationPersonnel> dataList = findAllByOrderByRecordDateDesc();
            if (dataList == null || dataList.isEmpty()) {
                log.error("데이터를 가져오지 못했습니다.");
                return;
            }

            // 데이터셋 생성
            List<DataSet> dataSetList = new ArrayList<>();
            for (int i = 0; i < dataList.size() - 1; i++) {
                TimeStationPersonnel currentData = dataList.get(i);
                TimeStationPersonnel nextData = dataList.get(i + 1);

                double[][][] inputArray = getDataArray(currentData);
                double[][][] outputArray = getDataArray(nextData);

                INDArray input = Nd4j.create(inputArray);
                INDArray output = Nd4j.create(outputArray);

                dataSetList.add(new DataSet(input, output));
            }

            log.info("DataSet 설정 완료");
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
                    .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)// 두 번째 레이어 추가, RNN 출력 레이어
                            .activation(Activation.IDENTITY)// 활성화 함수로 항등 사용
                            .nIn(lstmLayerSize)// 입력 수 설정
                            .nOut(numOutputs)// 출력 수 설정
                            .build())
                    .build();
            log.info("신경망 구성 설정 완료");
            
            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();

            // 신경망 초기화 및 학습
            log.info("신경망 초기화 및 학습 시작...");
            net.setListeners(new ScoreIterationListener(1));

            // EarlyStoppingTrainer 생성 및 학습
            EarlyStoppingConfiguration<MultiLayerNetwork> esConf = new EarlyStoppingConfiguration.Builder<MultiLayerNetwork>()
                    .epochTerminationConditions(new MaxEpochsTerminationCondition(numEpochs))
                    .evaluateEveryNEpochs(1)
                    .iterationTerminationConditions(new MaxScoreIterationTerminationCondition(5))
                    .scoreCalculator(new DataSetLossCalculator(iterator, true))
                    .modelSaver(new LocalFileModelSaver("./saved_model"))
                    .build();
            EarlyStoppingTrainer trainer = new EarlyStoppingTrainer(esConf, net, iterator);

            trainer.fit();

            log.info("신경망 초기화 및 학습 완료");

            evaluateModel(iterator, numOutputs, net);

            File modelFile = new File("MTDMProvider.zip");
            ModelSerializer.writeModel(net, modelFile, true);
        } catch (IOException e) {
            log.error("학습 중 파일 작성 오류", e);
        } catch (DataAccessException e) {
            log.error("데이터 엑세스 중 오류 발생, 데이터베이스 연결이 실패했습니다.", e);
        } catch (Exception e) {
            log.error("학습 중 에러 발생", e);
        }
    }

    // TimeStationPersonnel 객체에서 시간대별 인원 수를 가져와서 double 배열로 반환하는 메서드
    public double[][][] getDataArray(TimeStationPersonnel data) {
        return new double[][][] {
                {{data.getSix()}}, {{data.getSeven()}}, {{data.getEight()}}, {{data.getNine()}}, {{data.getTen()}},
                {{data.getEleven()}}, {{data.getTwelve()}}, {{data.getThirteen()}}, {{data.getFourteen()}}, {{data.getFifteen()}},
                {{data.getSixteen()}}, {{data.getSeventeen()}}, {{data.getEighteen()}}, {{data.getNineteen()}}, {{data.getTwenty()}},
                {{data.getTwentyOne()}}, {{data.getTwentyTwo()}}, {{data.getFromTwentyThreeToSixHour()}}
        };
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

            TimeStationPersonnel previousData = timeStationPersonnelRepository.findGetLastRegistData(targetLine, targetStation, targetDate);
            if (previousData == null) {
                log.error("이전 데이터를 찾을 수 없습니다.");
                return;
            }

            double[][][] inputArray = getDataArray(previousData);
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
}

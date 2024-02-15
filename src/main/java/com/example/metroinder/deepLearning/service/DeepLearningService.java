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
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Service;

import java.io.File;
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
            List<TimeStationPersonnel> dataList = findAllByOrderByRecordDateDesc();
            // 하이퍼파라미터 설정
            int batchSize = Math.min(64, dataList.size() - 1);
            int numInputs = 1; // 입력 변수 수 (하나의 시계열 데이터를 사용하기 때문에 1)
            int numOutputs = 1; // 출력 변수 수 (다음 시간대의 승하차 인원 예측을 위해 1)
            int lstmLayerSize = 50; // LSTM 레이어의 크기
            int numEpochs = 6; // 에폭


            if (dataList.isEmpty()) {
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
                    .seed(123)
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                    .weightInit(WeightInit.XAVIER)
                    .updater(new Adam())
                    .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
                    .gradientNormalizationThreshold(0.5)
                    .list()
                    .layer(new LSTM.Builder()
                            .nIn(numInputs)
                            .nOut(lstmLayerSize)
                            .activation(Activation.TANH)
                            .build())
                    .layer(new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                            .activation(Activation.IDENTITY)
                            .nIn(lstmLayerSize)
                            .nOut(numOutputs)
                            .build())
                    .build();
            log.info("신경망 구성 설정 완료");
            log.info("신경망 초기화 및 학습 시작...");
            // 신경망 초기화 및 학습
            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            net.setListeners(new ScoreIterationListener(1));
            net.fit(iterator, numEpochs);
            log.info("신경망 초기화 및 학습 완료");
            // 특정 호선과 특정 역에 대한 예측을 위한 입력 데이터 생성
            String targetLine = "1"; //임시
            String targetStation = "서울역"; //임시
            String targetDate = "2023-10-31"; //YYYY-MM-DD 형식

            // 특정 호선과 특정 역에 대한 입력 데이터 검색

            TimeStationPersonnel targetData = timeStationPersonnelRepository.findGetLastRegistData(targetLine, targetStation, targetDate);

            // 입력 데이터로 사용할 시간대별 승하차 인원 배열 생성
            double[][][] inputArray = getDataArray(targetData);
            INDArray input = Nd4j.create(inputArray);

            // 모델을 사용하여 예측 수행
            INDArray output = net.output(input);

            log.info("다음 시간대의 예측된 승하차 인원: {}", output);

            /*File modelFile = new File("trained_model.zip");
            ModelSerializer.writeModel(net, modelFile, true);*/
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // TimeStationPersonnel 객체에서 시간대별 인원 수를 가져와서 double 배열로 반환하는 메서드
    public double[][][] getDataArray(TimeStationPersonnel data) {
        return new double[][][] {
                {{data.getSix()}}, {{data.getSeven()}}, {{data.getEight()}}, {{data.getNine()}}, {{data.getTen()}},
                {{data.getEleven()}}, {{data.getTwelve()}}, {{data.getThirteen()}}, {{data.getFourteen()}}, {{data.getFifteen()}},
                {{data.getSixteen()}}, {{data.getSeventeen()}}, {{data.getEighteen()}}, {{data.getNineteen()}}, {{data.getTwenty()}},
                {{data.getTwentyOne()}}, {{data.getTwentyTwo()}}, {{data.getFromTwentyThreeToSixHour()}},
                {{data.getSix()}}, {{data.getSeven()}}, {{data.getEight()}}, {{data.getNine()}}, {{data.getTen()}}, {{data.getEleven()}}
        };
    }
    public void predict(String targetLine, String targetStation, String targetDate) {
        try {
            // 저장된 모델 로드
            File modelFile = new File("trained_model.zip");
            MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(modelFile);
            log.info("모델 로드 완료: {}", modelFile.getAbsolutePath());

            TimeStationPersonnel previousData = timeStationPersonnelRepository.findGetLastRegistData(targetLine, targetStation, targetDate);
            double[][][] inputArray = getDataArray(previousData);
            INDArray input = Nd4j.create(inputArray);

            // 모델을 사용하여 다음 시간대의 데이터 예측
            INDArray output = net.output(input);

            log.info("다음 시간대의 예측된 승하차 인원: {}", output);
        } catch (Exception e) {
            log.error("모델 로드 실패", e);
        }
    }
}

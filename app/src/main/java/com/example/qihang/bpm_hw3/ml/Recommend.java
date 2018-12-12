package com.example.qihang.bpm_hw3.ml;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.os.SystemClock;
import android.util.Log;

import com.example.qihang.bpm_hw3.network.model.OutpatientDoctor;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by qihang on 2018/12/10.
 */

public class Recommend {
    private Interpreter tflite;

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "Recommend Classifier";

    /**
     * Name of the model file stored in Assets.
     */
    private static final String MODEL_PATH = "model.tflite";

    /**
     * Name of the label file stored in Assets.
     */
    private static final String LABEL_PATH = "labels.txt";

    /**
     * Labels corresponding to the output of the vision model.
     */
    private List<String> labelList;

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     */
    private float[][] labelProbArray = null;


    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;

    /**
     * A PriorityQueue that sort the result by it weights
     */
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels = new PriorityQueue<>(RESULTS_TO_SHOW, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

    private static Map<String, OutpatientDoctor> maps = new HashMap<>();

    /*
     * predefined outpatient data
     */
    static {
        maps.put("1544598271778", new OutpatientDoctor("1544598271778", "内科医生 1", "专治哮喘，腹泻等内科疾病"));
        maps.put("1544598285944", new OutpatientDoctor("1544598285944", "内科医生 2", "专治胃病胀气，咳嗽等一系列小疾病"));
        maps.put("1544598307751", new OutpatientDoctor("1544598307751", "内科医生 3", "呼吸困难，哮喘等呼吸道疾病"));
        maps.put("1544598327889", new OutpatientDoctor("1544598327889", "外科医生 1", "肩部，手腕等摔伤，扭伤"));
        maps.put("1544598349871", new OutpatientDoctor("1544598349871", "外科医生 2", "膝关节，半月板等膝盖疾病，锻炼后恢复"));
        maps.put("1544598363801", new OutpatientDoctor("1544598363801", "外科医生 3", "专治手腕扭伤"));
        maps.put("1544598386333", new OutpatientDoctor("1544598386333", "眼科医生 1", "专治眼球干涩，近视等眼科疾病"));
        maps.put("1544598400334", new OutpatientDoctor("1544598400334", "眼科医生 2", "专治视力下降"));
        maps.put("1544598442038", new OutpatientDoctor("1544598442038", "眼科医生 3", "眼部保养等眼科问题"));
        maps.put("1544598455348", new OutpatientDoctor("1544598455348", "耳科医生 1", "专治耳鸣等儿科疾病"));
    }

    /**
     * Init the Recommend Classifier
     *
     * @param activity the context
     * @throws IOException File is not existed
     */
    public Recommend(Activity activity) throws IOException {
        tflite = new Interpreter(loadModelFile(activity));
        labelList = loadLabelList(activity);
        labelProbArray = new float[1][labelList.size()];
        Log.i(TAG, "Created a Tensorflow Lite Classifier");
    }

    /**
     * Predict the result
     *
     * @return Top K result
     */
    public List<OutpatientDoctor> predict(float[] input) {
        long startTime = SystemClock.uptimeMillis();
        tflite.run(input, labelProbArray);
        long endTime = SystemClock.uptimeMillis();

        Log.i(TAG, "Predict Time (ms) " + (endTime - startTime));
        return printTopKLabels();
    }

    /**
     * Prints top-K labels, to be shown in   UI as the results.
     */
    private List<OutpatientDoctor> printTopKLabels() {
        for (int i = 0; i < labelList.size(); ++i) {
            sortedLabels.add(new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        List<OutpatientDoctor> list = new ArrayList<>();
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            list.add(maps.get(label.getKey()));
        }
        return list;
    }

    /**
     * Memory-map the model file in Assets.
     */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * Reads label list from Assets.
     */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

}

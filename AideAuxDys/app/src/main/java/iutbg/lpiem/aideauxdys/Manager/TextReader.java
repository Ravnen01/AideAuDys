package iutbg.lpiem.aideauxdys.Manager;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TextReader implements TextToSpeech.OnInitListener{
    private TextToSpeech textToSpeech;
    private Context context;
    private boolean isInit;

    public TextReader(Context context) {
        this.context = context;
        isInit = false;
        textToSpeech = new TextToSpeech(context, this);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isInit = true;
        }
    }

    public void say(String text){
        if(isInit){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public boolean isInit() {
        return isInit;
    }

    public boolean isSpeaking(){
        return textToSpeech.isSpeaking();
    }

    public void stopSpeaking(){
        textToSpeech.stop();
    }

    public void destroy(){
        textToSpeech.shutdown();
    }
}

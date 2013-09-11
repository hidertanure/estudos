package br.com.hider.onlyone.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.com.hider.onlyone.activity.MainActivity;
import br.com.hider.onlyone.model.business.BusinessController;
import br.com.hider.onlyone.model.entity.AppType;

public class MessageInterceptorService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        final int eventType = event.getEventType();

        switch (eventType) {

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                Notification notification = (Notification) event.getParcelableData();

                @SuppressLint("NewApi") RemoteViews views = notification.bigContentView;
                Class<?> secretClass = views.getClass();

                ArrayList<String> raw = new ArrayList<String>();

                Field outerFields[] = secretClass.getDeclaredFields();
                for (int i = 0; i < outerFields.length; i++) {
                    if (outerFields[i].getName().equals("mActions")) {
                        outerFields[i].setAccessible(true);

                        ArrayList<Object> actions = null;
                        try {
                            actions = (ArrayList<Object>) outerFields[i].get(views);

                            for (Object action : actions) {
                                Field innerFields[] = action.getClass()
                                        .getDeclaredFields();

                                Object value = null;
                                Integer type = null;
                                for (Field field : innerFields) {
                                    try {
                                        field.setAccessible(true);
                                        if (field.getName().equals("type")) {
                                            type = field.getInt(action);
                                        } else if (field.getName().equals("value")) {
                                            value = field.get(action);
                                        }
                                    } catch (IllegalArgumentException e) {
                                    } catch (IllegalAccessException e) {
                                    }
                                }

                                if (type != null && type == 10 && value != null) {
                                    raw.add(value.toString());
                                }
                            }
                        } catch (IllegalArgumentException e1) {
                        } catch (IllegalAccessException e1) {
                        }
                    }
                }

                parseRawMessages(event.getPackageName().toString(), raw);

                break;
        }
    }

    private void parseRawMessages(String pack, ArrayList<String> raw) {

        //Hangouts, single user.
        //0 - Xtrume ES
        //1 - hidertanure@gmail.com
        //2 - Alo!

        //Whatsapp, single user.
        //0 - Vitor Bastos
        //1 - 1 mensagem nova.
        //2 - Huahua

        AppType apptype;

        if(pack.startsWith("com.google.android.talk")){
            apptype = AppType.HANGOUTS;
        }else{
            apptype = AppType.WHATS;
        }


        BusinessController businessController = new BusinessController();
        businessController.newMessage(this.getApplicationContext(), apptype, raw);

        Intent intent = new Intent(MainActivity.ONLY_ONE_MESSAGE);
        intent.setAction(MainActivity.ONLY_ONE_MESSAGE);
        intent.putStringArrayListExtra("content", raw);
        sendBroadcast(intent);

    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected() {

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // We are interested in all types of accessibility events.
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        // We want to provide specific type of feedback.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        // We want to receive events in a certain interval.
        info.notificationTimeout = 100;
        // We want to receive accessibility events only from certain packages.

        // only handle this package
        info.packageNames = new String[] { "com.google.android.talk", "com.whatsapp" };

        setServiceInfo(info);

    }
}

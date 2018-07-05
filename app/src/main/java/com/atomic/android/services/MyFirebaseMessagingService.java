/*
 *  Copyright 2017 Rozdoum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.atomic.android.services;

import com.atomic.android.utils.LogUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by alexey on 13.04.17.
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    private static final String ACTION_TYPE_KEY = "actionType";
    private static final String ACTION_TYPE_NEW_LIKE = "new_like";
    private static final String ACTION_TYPE_NEW_COMMENT = "new_comment";
    private static final String ACTION_TYPE_NEW_POST = "new_post";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null && remoteMessage.getData().get(ACTION_TYPE_KEY) != null) {
            handleRemoteMessage(remoteMessage);
        } else {
            LogUtil.logError(TAG, "onMessageReceived()", new RuntimeException("FCM remoteMessage doesn't contains Action Type"));
        }
    }

    private void handleRemoteMessage(RemoteMessage remoteMessage) {
        String receivedActionType = remoteMessage.getData().get(ACTION_TYPE_KEY);
        LogUtil.logDebug(TAG, "Message Notification Action Type: " + receivedActionType);

        switch (receivedActionType) {
            case ACTION_TYPE_NEW_LIKE:
                parseCommentOrLike(remoteMessage);
                break;
            case ACTION_TYPE_NEW_COMMENT:
                parseCommentOrLike(remoteMessage);
                break;
            case ACTION_TYPE_NEW_POST:
                handleNewPostCreatedAction(remoteMessage);
                break;
        }
    }

    private void handleNewPostCreatedAction(RemoteMessage remoteMessage) {


    }

    private void parseCommentOrLike(RemoteMessage remoteMessage) {

    }



}

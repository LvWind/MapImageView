/*
 * Copyright (c) 2017. Jason Shaw <lvwind.shaw@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lvwind.mapimageview;


import java.util.LinkedList;

final class RequestList {

    private final static RequestList instance = new RequestList();
    private static final int MAX_THREAD = 5;

    static {
        instance.startDownloads();
    }

    private final LinkedList<Request> requestQueue = new LinkedList<Request>();
    private final DownloadThread[] threadPool;

    private RequestList() {
        threadPool = new DownloadThread[MAX_THREAD];
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i] = new DownloadThread("Worker-" + i, this);
        }
    }

    static RequestList getInstance() {
        return instance;
    }

    synchronized void removeQueueAll() {
        requestQueue.clear();
    }

    void startDownloads() {
        for (DownloadThread thread : threadPool) {
            thread.start();
        }
    }

    void stopDownloads() {
        for (DownloadThread thread : threadPool) {
            thread.interrupt();
        }
    }

    synchronized void putRequest(Request request, Priority priority) {
        if (priority == Priority.HIGH) {
            requestQueue.addFirst(request);
        } else {
            requestQueue.addLast(request);
        }
        notifyAll();
    }

    synchronized Request takeRequest() {
        while (requestQueue.size() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return requestQueue.poll();
    }

    public enum Priority {
        HIGH, LOW
    }
}

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

import java.io.File;


class Request {
    private final String url;
    private final File cacheDir;
    private final Runnable runnable;
    private Status status = Status.WAIT;

    Request(String url, File cacheDir) {
        this.url = url;
        this.cacheDir = cacheDir;
        this.runnable = getDefaultRunnable();
    }

    Request(String url, File cacheDir, Runnable runnable) {
        this.url = url;
        this.cacheDir = cacheDir;
        this.runnable = runnable;
    }

    synchronized Status getStatus() {
        return status;
    }

    synchronized void setStatus(Status status) {
        this.status = status;
    }

    String getUrl() {
        return url;
    }

    File getCacheDir() {
        return cacheDir;
    }

    Runnable getRunnable() {
        return (runnable != null) ? runnable : getDefaultRunnable();
    }

    private Runnable getDefaultRunnable() {
        return new Runnable() {
            public void run() {
            }
        };
    }

    public enum Status {
        WAIT, LOADING, LOADED
    }
}

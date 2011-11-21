/**
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
 */
package geomobility.core.net;

public abstract class TaskPool {
    public abstract void run();

    /**
     * Execute a given {@link TaskPool} and return the status through
     * {@link TaskCallback}.
     *
     * @param callback
     *            An instance of {@link TaskCallback} which help inform the
     *            status of {@link TaskPool}.
     */
    protected void execute(TaskCallback callback) {
        try {
            // Run the task first
            run();
            if (callback != null) {
                // success
                callback.onDone(null);
            }
        } catch (Exception ex) {
            if (callback != null) {
                // failure
                callback.onDone(ex);
            }
        }
    }

    /**
     * Inform whenever a {@link TaskPool} is execute success or not.
     *
     * @author Hieu.Rocker
     */
    public static interface TaskCallback {
        /**
         * This method called whenever the {@link TaskPool} is execute success
         * or not.
         *
         * @param ex
         *            Null if the {@link TaskPool} is execute success,
         *            otherwise failed with an exception.
         */
        public void onDone(Exception ex);
    }
}

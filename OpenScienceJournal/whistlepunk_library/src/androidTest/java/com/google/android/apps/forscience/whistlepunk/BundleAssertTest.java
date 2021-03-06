/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.android.apps.forscience.whistlepunk;

import android.os.Bundle;
import android.test.AndroidTestCase;

public class BundleAssertTest extends AndroidTestCase {
    public void testAssertBundlesEqualMissingKey() {
        try {
            final Bundle hasKey = new Bundle();
            hasKey.putString("key", "value");
            final Bundle noKey = new Bundle();
            BundleAssert.assertBundlesEqual(hasKey, noKey);
        } catch (AssertionError expected) {
            return;
        }
        fail("Should have failed!");
    }

    public void testAssertBundlesEqualDifferentValues() {
        try {
            final Bundle a = new Bundle();
            a.putString("key", "a");
            final Bundle b = new Bundle();
            b.putString("key", "b");
            BundleAssert.assertBundlesEqual(a, b);
        } catch (AssertionError expected) {
            return;
        }
        fail("Should have failed!");
    }
}

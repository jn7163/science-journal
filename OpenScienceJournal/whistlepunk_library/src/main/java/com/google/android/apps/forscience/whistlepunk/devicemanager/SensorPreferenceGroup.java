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
package com.google.android.apps.forscience.whistlepunk.devicemanager;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;

import com.google.android.apps.forscience.whistlepunk.R;
import com.google.android.apps.forscience.whistlepunk.SensorAppearanceProvider;
import com.google.android.apps.forscience.whistlepunk.api.scalarinput.InputDeviceSpec;

import java.util.List;

// TODO: delete when new fragment is ready
class SensorPreferenceGroup implements SensorGroup {
    private PreferenceScreen mScreen;
    private final PreferenceCategory mCategory;
    private boolean mRemoveWhenEmpty;
    private boolean mIncludeSummary;
    private final SensorAppearanceProvider mAppearanceProvider;

    public SensorPreferenceGroup(PreferenceScreen screen, PreferenceCategory category,
            boolean removeWhenEmpty, boolean includeSummary,
            SensorAppearanceProvider appearanceProvider) {
        mScreen = screen;
        mCategory = category;
        mRemoveWhenEmpty = removeWhenEmpty;
        mIncludeSummary = includeSummary;
        mAppearanceProvider = appearanceProvider;
        mCategory.setOrderingAsAdded(false);
    }

    @Override
    public boolean hasSensorKey(String sensorKey) {
        return mCategory.findPreference(sensorKey) != null;
    }

    @Override
    public void addSensor(String sensorKey, ConnectableSensor sensor) {
        if (sensor.isBuiltIn()) {
            // This view doesn't handle built-in sensors
            return;
        }
        addPreference(buildFullPreference(sensorKey, sensor));
    }

    @NonNull
    private Preference buildFullPreference(String sensorKey, ConnectableSensor sensor) {
        Preference pref = buildAvailablePreference(sensorKey, sensor);
        if (mIncludeSummary) {
            pref.setWidgetLayoutResource(R.layout.preference_external_device);
            pref.setSummary(sensor.getSpec().getSensorAppearance().getName(pref.getContext()));
        }
        return pref;
    }

    private void addPreference(Preference preference) {
        if (mRemoveWhenEmpty && !isOnScreen()) {
            mScreen.addPreference(mCategory);
        }
        mCategory.addPreference(preference);
    }

    private boolean isOnScreen() {
        return mScreen.findPreference(mCategory.getKey()) != null;
    }

    @NonNull
    private Preference buildAvailablePreference(String key, ConnectableSensor sensor) {
        Context context = mCategory.getContext();
        Preference pref = new Preference(context);
        pref.setTitle(sensor.getAppearance(mAppearanceProvider).getName(context));
        pref.setKey(key);
        return pref;
    }

    @Override
    public boolean removeSensor(String key) {
        Preference preference = mCategory.findPreference(key);
        if (preference != null) {
            mCategory.removePreference(preference);
        }
        if (mRemoveWhenEmpty && mCategory.getPreferenceCount() == 0 && isOnScreen()) {
            mCategory.removePreference(mCategory);
        }
        return preference != null;
    }

    @Override
    public void replaceSensor(String sensorKey, ConnectableSensor sensor) {
        Preference oldPref = mCategory.findPreference(sensorKey);
        if (oldPref == null) {
            addSensor(sensorKey, sensor);
        } else {
            mCategory.removePreference(oldPref);
            Preference newPref = buildFullPreference(sensorKey, sensor);
            newPref.setOrder(oldPref.getOrder());
            // TODO: can I test this directly?
            mCategory.addPreference(newPref);
        }
    }

    @Override
    public int getSensorCount() {
        return mCategory.getPreferenceCount();
    }


    @Override
    public void addAvailableService(String providerId,
            ExternalSensorDiscoverer.DiscoveredService service, boolean startSpinners) {
        // This view doesn't track services
    }

    @Override
    public void onServiceScanComplete(String serviceId) {
        // This view doesn't track services
    }

    @Override
    public void addAvailableDevice(ExternalSensorDiscoverer.DiscoveredDevice device) {
        // This view doesn't track devices
    }

    @Override
    public void setMyDevices(List<InputDeviceSpec> device) {
        // This view doesn't track devices
    }

    @Override
    public boolean addAvailableSensor(String sensorKey, ConnectableSensor sensor) {
        // This view doesn't track devices
        return false;
    }

    @Override
    public void onSensorAddedElsewhere(String newKey, ConnectableSensor sensor) {

    }
}

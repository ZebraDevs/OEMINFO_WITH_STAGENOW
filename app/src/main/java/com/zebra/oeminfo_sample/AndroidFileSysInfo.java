package com.zebra.oeminfo_sample;

import android.os.Build;
import android.util.SparseArray;

import java.util.Locale;

/**
 * <p>System Users and Groups for the Android platform as specified in
 * <a href="https://android.googlesource.com/platform/system/core/+/master/include/private/android_filesystem_config.h">android_filesystem_config.h</a>.</p>
 *
 * <p>Last updated: April 20th, 2016</p>
 *
 * <p><b>Note:</b> Some OEMs may have specific UIDs for other system users not in this class.</p>
 */
public class AndroidFileSysInfo {

    /* first app user */
    private static final int AID_APP = 10000;

    /* offset for uid ranges for each user */
    private static final int AID_USER = 100000;

    /* start of gids for apps in each user to share */
    private static final int AID_SHARED_GID_START = 50000;

    /* end of gids for apps in each user to share */
    private static final int AID_SHARED_GID_END = 59999;

    private static final SparseArray<String> SYSTEM_IDS = new SparseArray<>();

    static {
        putSystemId(0, "root"); /* traditional unix root user */
        putSystemId(1000, "system"); /* system server */
        putSystemId(1001, "radio"); /* telephony subsystem, RIL */
        putSystemId(1002, "bluetooth"); /* bluetooth subsystem */
        putSystemId(1003, "graphics"); /* graphics devices */
        putSystemId(1004, "input"); /* input devices */
        putSystemId(1005, "audio"); /* audio devices */
        putSystemId(1006, "camera"); /* camera devices */
        putSystemId(1007, "log"); /* log devices */
        putSystemId(1008, "compass"); /* compass device */
        putSystemId(1009, "mount"); /* mountd socket */
        putSystemId(1010, "wifi"); /* wifi subsystem */
        putSystemId(1011, "adb"); /* android debug bridge (adbd) */
        putSystemId(1012, "install"); /* group for installing packages */
        putSystemId(1013, "media"); /* mediaserver process */
        putSystemId(1014, "dhcp"); /* dhcp client */
        putSystemId(1015, "sdcard_rw"); /* external storage write access */
        putSystemId(1016, "vpn"); /* vpn system */
        putSystemId(1017, "keystore"); /* keystore subsystem */
        putSystemId(1018, "usb"); /* USB devices */
        putSystemId(1019, "drm"); /* DRM server */
        putSystemId(1020, "mdnsr"); /* MulticastDNSResponder (service discovery) */
        putSystemId(1021, "gps"); /* GPS daemon */
        // 1022 is deprecated and not used.
        putSystemId(1023, "media_rw"); /* internal media storage write access */
        putSystemId(1024, "mtp"); /* MTP USB driver access */
        // 1025 is deprecated and not used.
        putSystemId(1026, "drmrpc"); /* group for drm rpc */
        putSystemId(1027, "nfc"); /* nfc subsystem */
        putSystemId(1028, "sdcard_r"); /* external storage read access */
        putSystemId(1029, "clat"); /* clat part of nat464 */
        putSystemId(1030, "loop_radio"); /* loop radio devices */
        putSystemId(1031, "mediadrm"); /* MediaDrm plugins */
        putSystemId(1032, "package_info"); /* access to installed package details */
        putSystemId(1033, "sdcard_pics"); /* external storage photos access */
        putSystemId(1034, "sdcard_av"); /* external storage audio/video access */
        putSystemId(1035, "sdcard_all"); /* access all users external storage */
        putSystemId(1036, "logd"); /* log daemon */
        putSystemId(1037, "shared_relro"); /* creator of shared GNU RELRO files */
        putSystemId(1038, "dbus"); /* dbus-daemon IPC broker process */
        putSystemId(1039, "tlsdate"); /* tlsdate unprivileged user */
        putSystemId(1040, "mediaex"); /* mediaextractor process */
        putSystemId(1041, "audioserver"); /* audioserver process */
        putSystemId(1042, "metrics_coll"); /* metrics_collector process */
        putSystemId(1043, "metricsd"); /* metricsd process */
        putSystemId(1044, "webserv"); /* webservd process */
        putSystemId(1045, "debuggerd"); /* debuggerd unprivileged user */
        putSystemId(1046, "mediacodec"); /* mediacodec process */
        putSystemId(1047, "cameraserver"); /* cameraserver process */
        putSystemId(1048, "firewall"); /* firewalld process */
        putSystemId(1049, "trunks"); /* trunksd process (TPM daemon) */
        putSystemId(1050, "nvram"); /* Access-controlled NVRAM */
        putSystemId(1051, "dns"); /* DNS resolution daemon (system: netd) */
        putSystemId(1052, "dns_tether"); /* DNS resolution daemon (tether: dnsmasq) */
        putSystemId(1053, "webview_zygote"); /* WebView zygote process */
        putSystemId(1054, "vehicle_network"); /* Vehicle network service */
        putSystemId(1055, "media_audio"); /* GID for audio files on internal media storage */
        putSystemId(1056, "media_video"); /* GID for video files on internal media storage */
        putSystemId(1057, "media_image"); /* GID for image files on internal media storage */

        putSystemId(2000, "shell"); /* adb and debug shell user */
        putSystemId(2001, "cache"); /* cache access */
        putSystemId(2002, "diag"); /* access to diagnostic resources */

        /* The range 2900-2999 is reserved for OEMs */

        // The 3000 series are intended for use as supplemental group id's only. They indicate
        // special Android capabilities that the kernel is aware of.
        putSystemId(3001, "net_bt_admin"); /* bluetooth: get any socket */
        putSystemId(3002, "net_bt"); /* bluetooth: get sco, rfcomm or l2cap sockets */
        putSystemId(3003, "inet"); /* can get AF_INET and AF_INET6 sockets */
        putSystemId(3004, "net_raw"); /* can get raw INET sockets */
        putSystemId(3005, "net_admin"); /* can configure interfaces and routing tables. */
        putSystemId(3006, "net_bw_stats"); /* read bandwidth statistics */
        putSystemId(3007, "net_bw_acct"); /* change bandwidth statistics accounting */
        putSystemId(3008, "net_bt_stack"); /* bluetooth: access config files */
        putSystemId(3009, "readproc"); /* Allow /proc read access */
        putSystemId(3010, "wakelock"); /* Allow system wakelock read/write access */

        /* The range 5000-5999 is also reserved for OEMs. */

        putSystemId(9997, "everybody"); /* shared between all apps in the same profile */
        putSystemId(9998, "misc"); /* access to misc storage */
        putSystemId(9999, "nobody");
    }

    private static void putSystemId(int id, String name) {
        // Check if the uid exists before adding it so we don't add unsupported ids.
        if (android.os.Process.getUidForName(name) != id) {
            // Not valid on this system. Most likely due to a lower API.
            return;
        }
        SYSTEM_IDS.put(id, name);
    }

    /**
     * @return An array of system UIDs
     */
    public static SparseArray<String> getSystemIds() {
        return SYSTEM_IDS;
    }

    /**
     * Returns the UID/GID name assigned to a particular id, or {@code null} if there is none.
     *
     * @param id
     *     The UID/GID of a process or file
     * @return the name of the UID/GID or {@code null} if the id is unrecognized.
     */
    public static String getNameForId(int id) {
        String name = SYSTEM_IDS.get(id);
        if (name == null) {
            if (id >= AID_SHARED_GID_START && id <= AID_SHARED_GID_END) {
                name = String.format(Locale.ENGLISH, "all_a%d", id - AID_SHARED_GID_START);
            } else {
                int appId = id - AID_APP;
                int userId = 0;
                // loop until we get the correct user id.
                // 100000 is the offset for each user.
                while (appId > AID_USER) {
                    appId -= AID_USER;
                    userId++;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                    name = String.format(Locale.ENGLISH, "u%d_a%d", userId, appId);
                } else {
                    // app_{app_id} is used below API 17.
                    name = String.format(Locale.ENGLISH, "app_%d", appId);
                }
            }
        }
        return name;
    }

    private AndroidFileSysInfo() {
        throw new AssertionError("no instances");
    }

}
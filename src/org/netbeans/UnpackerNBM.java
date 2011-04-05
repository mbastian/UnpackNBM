/*
Copyright (c) 2011, Gephi.org
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
3. Neither the name of Gephi.org; nor the names of its
contributors may be used to endorse or promote products derived from this
software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.netbeans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility class to unpack NBM files.
 * <p>
 * NMB files are plug-in packages created by Netbeans Platform. Besides the plug-in
 * JARs, the NMB files includes other information like plug-in dependencies and version.
 * <P>
 * This class unpack the JARs compressed in a NBM package, using unpack200 installed
 * on the system.
 *
 * @author Mathieu Bastian
 */
public class UnpackerNBM {

    public void unpack(File nbm) throws Exception {
        //Unzip, get several pack.gz files
        List<File> packFiles = unzip(nbm);
        if (packFiles.isEmpty()) {
            throw new IOException("Impossible to find a pack.gz archive in the NBM file");
        }

        //Unpack each pack.gz files
        for (File f : packFiles) {
            if (f.getName().endsWith("pack.gz")) {
                unpack200(f);
                f.delete();
            } else {
                System.out.println("Unpack "+f.getName());
            }
        }
    }

    private void unpack200(File pack) throws Exception {
        String baseName = pack.getName().substring(0, pack.getName().lastIndexOf(".jar.pack.gz"));
        File jarFile = new File(pack.getParentFile(), baseName + ".jar");
        System.out.println("Unpack " + baseName + ".jar");

        String unpack200Executable = new File(System.getProperty("java.home"), "bin/unpack200" + (isWindows() ? ".exe" : "")).getAbsolutePath();

        ProcessBuilder pb = new ProcessBuilder(
                unpack200Executable,
                pack.getAbsolutePath(),
                jarFile.getAbsolutePath());

        pb.directory(pack.getParentFile());
        int result;
        Process process = pb.start();
        result = process.waitFor();
        process.destroy();
        if (result != 0) {
            throw new RuntimeException("Could not find unpack200, be sure you  have installed the JDK");
        }
    }

    private List<File> unzip(File nbm) throws Exception {

        List<File> packFiles = new ArrayList<File>();
        ZipFile zipFile;
        // Open Zip file for reading
        zipFile = new ZipFile(nbm, ZipFile.OPEN_READ);

        // Create an enumeration of the entries in the zip file
        Enumeration zipFileEntries = zipFile.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

            String currentEntry = entry.getName();
            //System.out.println(currentEntry);
            if (currentEntry.endsWith("pack.gz") || currentEntry.endsWith("jar")) {
                BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[2048];

                // write the current file to disk
                String baseName = currentEntry.substring(currentEntry.lastIndexOf('/') + 1);
                File packFile = new File(nbm.getParentFile(), baseName);
                packFiles.add(packFile);
                FileOutputStream fos = new FileOutputStream(packFile);
                BufferedOutputStream dest =
                        new BufferedOutputStream(fos, 2048);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, 2048)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }
        }
        zipFile.close();
        return packFiles;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name"); // NOI18N
        return (os != null && os.toLowerCase().startsWith("windows"));//NOI18N
    }
}

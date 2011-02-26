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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command-line application to unpack a NMB file. The application unpacks on the
 * same folder all the JARs in the NBM files.
 *
 * Usage: java -jar UnpackNBM.jar file
 *
 * @author Mathieu Bastian
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].endsWith("nbm")) {
            String path = args[0];
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("The file " + file.getAbsolutePath() + " doesn't exist");
            }
            UnpackerNBM unpacker = new UnpackerNBM();
            try {
                unpacker.unpack(file);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        String usage = "Usage: java -jar UnpackNBM.jar file";
        System.out.println(usage);
    }
}

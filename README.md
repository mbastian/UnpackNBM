# UnpackNBM

Command-line Java application to unpack a NMB file. The application unpacks on the
same folder all the JARs contained in a the given NBM file.

Use unpack200 installed on the system.

## Usage

Specify as an argument the NBM file to unpack.

    java -jar UnpackNBM.jar nbmfile

## Use for Gephi Toolkit

This utility application has been built to help Gephi Toolkit users to use Gephi
plug-ins with the [Gephi Toolkit](http://gephi.org/toolkit).

Gephi is built with [Netbeans Platform](http://platform.netbeans.org) and therefore
it's plug-ins are packaged as NBM files. Gephi Toolkit is a Java library that can be
used out of the box, and is packaged as a simple JAR. Because it is lightweight, the
Toolkit doesn't use NBM files. Therefore it is difficult to use the plug-ins, as one
need the real JARs compressed in NMBs.

This application unpacks the JARs files contained in a NBM file, in order to be used
by Gephi Toolkit users.

JARs can then simply be included in the classpath, like the gephi-toolkit.jar itself.

## Netbeans Modules (NBM)

NMB files are plug-in packages created by Netbeans Platform. Besides the plug-in 
JARs, the NMB files includes other information like plug-in dependencies and version.

More information on Netbean's website
http://wiki.netbeans.org/DevFaqWhatIsNbm

## Building

Be sure to have installed the JDK. The result is placed in the dist folder.

    ant jar

## Release notes

    v0.2: Add support for third-party JARs included in NBMs
    
    v0.1: First version

## License

Copyright © 2011 Mathieu Bastian, from Gephi.org

Distributed under the BSD license.
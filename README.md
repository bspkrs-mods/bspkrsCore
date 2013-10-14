bspkrsCore
=================
bspkrs' bspkrsCore dependency mod for Minecraft.  Contains the set of shared classes used in all mods released by bspkrs.
This repo contains source files for FML/Forge.

### Links of Interest
 - [Official Minecraft Forum Thread](http://www.minecraftforum.net/topic/1114612-)
 - [Downloads](http://bspk.rs/MC/bspkrsCore/index.html)
 - [Issue Tracking System](https://github.com/bspkrs/bspkrsCore/issues)
 
* * *

#### How to install and use the source code ####

1. Download the latest recommended [MinecraftForge](http://files.minecraftforge.net) src distribution.
2. Extract the Forge src zip file and run install.bat/.sh in the forge folder.
3. Clone this git repo to whatever locations you like.
4. Use the eclipse folder in your Forge/MCP setup as your Eclipse workspace.
5. Under the Minecraft project, add the "src" folder from the repo as a linked folder (rename as necessary) and set it as a source folder.

#### How to build from the source code ####

1. Download and install [Apache Ant](http://ant.apache.org) on your system. Make sure it is available on the path environment variable.
2. In the bspkrsCore repo folder, make a copy of build.properties_example and name it build.properties.
3. Edit the values in build.properties to contain valid paths on your system for each property. Details can be found in build.properties_example.
4. From a console window run "ant" from the bspkrsCore repo folder. The build will create its output in the bin folder.
5. Install the resulting mod archive by copying it to the mods folder on the client or server.

* * *

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.

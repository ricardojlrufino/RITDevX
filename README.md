<h1 align="center">
  <br>
  <img src="docs/images/logo.svg" alt="RITDevX">
  <br>
<!--
  <img src="https://img.shields.io/npm/l/stegcloak?style=plastic" />
  <a href="https://www.npmjs.com/package/stegcloak"> <img src="https://img.shields.io/npm/v/stegcloak?style=plastic" /> </a>
  <img src="https://badgen.net/badge/icon/terminal?icon=terminal&label" />
  <img src="https://img.shields.io/badge/code_style-standard-brightgreen.svg" />
-->	  
  <br>
  <a href="https://discord.gg/PKDxGQ" target="_blank"><img src="docs/images/discord.png" /></a>
</h1>
<h4 align="center">RITDevX - Rapid Interface Tool for Devices Experiments</h4>

RITDevX is a visual interface design tool, which allows the creation of interfaces to control and monitor real devices such as **Arduino** and **Raspberry**, allowing to develop experiments quickly.

It consists mainly of two modules:

- **Designer** - Which is the tool to create / design interface, where all aspects are configured.  
- **Controller** - It is the execution tool, which opens the layout defined by the Design module. It is responsible for communicating with devices using the [OpenDevice](https://github.com/OpenDevice/opendevice-lib-arduino) library (but can by extended). 
  This module can be *embedded* into an existing application or can run in *standalone mode*.  
- **Arduino IDE Plugin** - Enable easy integration with the Arduino IDE, and more advanced integration with this [alternative version of the Arduino IDE](https://github.com/ricardojlrufino/Arduino/releases).  ( **TODO ***)

  

![Demo](docs/images/intro.gif)

## Key features (Designer)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bd99fc0164cb405f9e995fe23789b62a)](https://app.codacy.com/manual/ricardojlrufino/RITDevX?utm_source=github.com&utm_medium=referral&utm_content=ricardojlrufino/RITDevX&utm_campaign=Badge_Grade_Dashboard)

- Drag and drop components  
- Drag and drop external images  
- Keyboard editing  
- Save / Open layout files  
- Allow rapid simulation ( need Controller )  
- Widgets  
  - [x] Images  
  - [ ] Icons (WIP)  
  - [ ] Gauges  
  - [ ] Levels   
  - [x] Charts (WIP)  

Please see: [docs/WORK_IN_PROGRESS.md](docs/WORK_IN_PROGRESS.md), to a full list of planned features and the current state

See:  [User Guide Designer Tutorial](https://opendevice.atlassian.net/wiki/spaces/RITDevX/pages/1581908126/User+Guide)

## Key features (Controller)

- Run layouts in standalone mode
- Log View
- Communication  
  - [x] Usb  
  - [ ] Bluetooth  
  - [ ] TCP / WiFi  


Please see: [docs/WORK_IN_PROGRESS.md](docs/WORK_IN_PROGRESS.md), to a full list of planned features and the current state

See:  [User Guide Controller Tutorial](https://opendevice.atlassian.net/wiki/spaces/RITDevX/pages/1581908143/User+Guide+Controller)

### Call for contributions

The project is in its early stages of development, and help from the community is always welcome.
Here are some points you can contribute:

- Developing new components / widgets
- Website creation (git-hub pages)
- Share of images for widgets, so that we can build an image database
- Please see file: [docs/WORK_IN_PROGRESS.md](docs/WORK_IN_PROGRESS.md)



## Download and Install

The project binaries are available in github releases.

TODO: ADD LINK



### Requirements

- Java 8  
- SO: Windows / Linux / MacOs  

### Build from Sources

	- `mvn package -Dmaven.test.skip=true`
	- This is a multi-module `maven` project, you can import (root pom.xml) your favorite IDE

## Docs

See documentation and examples at:

[RITDevX Wiki](https://opendevice.atlassian.net/wiki/spaces/RITDevX)
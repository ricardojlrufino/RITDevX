## RITDevX - Rapid Interface Tool for Devices Experiments



This document describes the planned requirements for the project and its current state

[PT_BR]  Esse documento descreve os requisitos planejados para o projeto e seu atual estado

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/740ff29ccbec4365a6d03c1d7d2b9d47)](https://www.codacy.com/manual/ricardojlrufino/RITDevX?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ricardojlrufino/RITDevX&amp;utm_campaign=Badge_Grade)

Desinger
========

### File Operations

- [x] Save
- [x] Save As
- [x] Open
- [x] NEW / Clear
- [x] Exit
- [ ] Exit (ask if as changes.)
- [ ] Warning for IMAGES not found on open file
- [ ] List of Recent files

### Editing Features

- [x] Drag and drop (external images)
- [x] Move Components
- [x] Move Components (using keyboard arrows)
- [x] Delete (Delete key / popup)
- [ ] Delete (ask before delete ?)
- [x] Clone
- [x] Rename
- [x] Show Grid
- [x] Double-click switch image state (ON/OFF)
- [ ] Grid Snap (using grid constraints)
- [x] Guide lines
- [ ] Snap / Guide lines with others components
- [ ] Auto-reload images on external changes (automatic ou from button ?).
- [x] Popup on right click (allow change file properties / limages)
- [ ] Open Image Editor (like gimp) on Popup Menu...
- [ ] Control + Z (Undo / Redo)
- [x] Detect transparent image shape / border

### Simulation

- [x] Start simulation (F9)
- [x] Disconect on close


### Visual

- [ ] Arduino IDE Plugin / Integration
- [x] Disable events table (not used for while...)
- [ ] Create menu / toolbar using icons.
- [ ] 2 layous (Compact / Two-Panel)
- [ ] Translation (implement controller/utils/I18n.java)
- [ ] Slash Screen / Icon


## Components / Widgets

- [ ] Documentation for widgets .. (generate HTML from markdown?!)
  NOTE: the documentation has to be clear that it uses OpenDevice in the protocol ...
- [ ] Allow creatate Containers, and set border, backgrouds and titles 
  Like: https://www.youtube.com/watch?v=DuEwOkOvlB0
  para quem tiver preguiça de desenhar, ou criar uma forma de update em tempo real.

### Images (on/off images)

- [x] On/Off images
- [ ] Operation modes 
  - [ ] None
  - [ ] Sensor
  - [x] Button
  - [ ] Push
- [x] CustomCmd  : Send values defined by user.   
- [x] Avoid click in transparent areas of image
- [ ] Pre-defined buttom images
- [ ] Contribuited images loaded from github

### Icons

- [x] Design Mode
- [ ] Execution Mode

- [x] On/Off images
- [ ] Operation modes 

### Charts (XCharts)

- [ ] Line
  - [x] Design Mode
  - [x] Execution Mode
- [ ] Graficos, precisa definir o atributo de onde ele obtem as linhas.   

### Gauges (SteelSeries)

Note: we need a big help here. This library has a multiple of configuration parameters, but leaving so many options confuses the user, an assessment needs to be made of which attributes are useful for each component.

- [x] Design Mode
- [ ] Design Mode Cleanup...
- [ ] Execution Mode

Levels (SteelSeries)

 - Level ( para criar barra de progressos, como temperatura, nível de água )

 - CircularKnobs ( controles circulares, como potenciômetros  ... quase finalizado )

   


Controller (Execution)
====================
- [ ] CLI - Run from cmd line 
- [x] Run / Simulate from Designer
- [x] Log View
   - [ ] Log View (auto-clear or use CircularBuffer to avoid memory leaks)
   - [ ] Save logs to file
- [ ] Reportar errros (https://www.bugsnag.com/open-source | https://sentry.io/for/open-source/)
- [x] **OpenDevice protocol support**
    - [x] Send
    - [x] Receive
    - [ ] Release OpenDevice (arduino and java library)
- [x] **Communication**
   - [x] Usb
   - [ ] Bluetooth
   - [ ] TCP / WIFI    
- [x] Support for sending commands (user commands)
- [x] Example with arduino
- [ ] Example of receipt with Esp8266
- [ ] Multiple charts to same DATA
   - Allow create multiple viwes, from same device.
   - Create shared datasource.
   - Attribute: Variable (allow use Var1,Var2,Var3)




## GENERAL TODO
======================

- [ ] REMOVE dependency: TreeMap<String, JComponent> addedComponents;
- [ ] Exepction dialog.
- [ ] Opção de Escolher o Layout da tela.

- [ ] Opção FULLSCREEN / FRAMELESS (no raspberry nem precisa .. mas no desktop eventualmente seria interessante )
- [ ] Suporte a multiplas telas (multiplos arquivos)?
- Usar a variavel Layer com uma configuração específica, permitindo alterar o layer (ao adicionar e editar)
- Tem que ficar claro que pode usar o teclado, teria que ter um efeito de SELECIONADO...
- user Scroll to resize... (if is resizable)
- JTableX, praticamente n serve para nada
- Nagegação de multiplos componentes... pode ser interessante suportar MUDANÇAS DE "PÁGINA"
- Criar utorial de como Extender e criar novos widegts JAVA e Apenas WidgetSkins
- User FONT-ICON, para componentes e indicadores...
   - como lsistar: https://github.com/jIconFont/jiconfont-catalog-javafx

  

- [ ] Interface remota raspberry - 
   Aqui seria o raspberry acessando uma pasta remota do PC, e monitorando ou recebendo notificações de alteração
   Se ateração for de imagens, recarrega tudo, se for possição é em realtime...

- [ ] Remo

- - [ ] GIF / MultiStateGiff, usar um gif onde cada frame é um estado do compoente.   
     Ou atributo de ANimado e não Animado


- [ ] Suporte a ZOOM: http://ayetgin.blogspot.com/2015/05/howto-zoom-inout-with-java-swing.html
       http://ayetgin.blogspot.com/2015/05/howto-zoom-inout-with-java-swing.html
- Criar função para mudar o Layer do componente   
- Medusa compoentn library (JavaFX): https://github.com/HanSolo/Medusa
- Permitir criar scripts....
- Remove: ScreenShot button




ImageSelectorDialog
- Dialogo que permite buscar no disco, e lista as imagens da internet
  curl -H "Content-Type: application/json"  https://api.github.com/repos/OpenDevice/OpenDevice/git/trees/master

- Extençção: xhmi


- Resizable SVG compoenents: 
    https://xmlgraphics.apache.org/batik/tools/browser.html (https://xmlgraphics.apache.org/batik/using/swing.html)
    https://xmlgraphics.apache.org/batik/tools/rasterizer.html




 - 


A parte de usabilidade e edição está ficando bem legal...

Segue alguns exemplos de utilização que estou testando.
---------------------------
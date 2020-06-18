#!/usr/bin/env bash

APPDIR="$(dirname -- "$(readlink -f -- "${0}")" )"
# parent:
APPDIR="$(dirname "$APPDIR")"

for LIB in \
    "$APPDIR"/java/lib/rt.jar \
    "$APPDIR"/java/lib/tools.jar \
    "$APPDIR"/lib/*.jar \
    ;
do
    CLASSPATH="${CLASSPATH}:${LIB}"
done
export CLASSPATH

LD_LIBRARY_PATH=$APPDIR/lib${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}
export LD_LIBRARY_PATH

export PATH="${APPDIR}/java/bin:${PATH}"

# Global Menu Support for Java Applications in Ubuntu
export JAVA_TOOL_OPTIONS=`echo $JAVA_TOOL_OPTIONS | sed 's|-javaagent:/usr/share/java/jayatanaag.jar||g'`

JAVA=java
if [ -x "$APPDIR/java/bin/java" ]; then
  JAVA=$APPDIR/java/bin/java
fi

# Collect options to java in an array, to properly handle whitespace in options
JAVA_OPTIONS=("-DAPP_DIR=$APPDIR")

# Only show the splash screen when no options are present
if [[ "$@" != *"--"* ]] ; then
    JAVA_OPTIONS+=("-splash:$APPDIR/bin/splash.png")
fi

"$JAVA" "${JAVA_OPTIONS[@]}" com.ricardojlrufino.ritdevx.designer.RIDesignerMain "$@"
#!/bin/bash

VERSION_tornado=2.4
URL_tornado=https://github.com/downloads/facebook/tornado/tornado-$VERSION.tar.gz
DEPS_tornado=(python)
MD5_tornado=c738af97c31dd70f41f6726cf0968941
BUILD_tornado=$BUILD_PATH/tornado/$(get_directory $URL_tornado)
RECIPE_tornado=$RECIPES_PATH/tornado

function prebuild_tornado() {
        cd $BUILD_tornado

        # check marker in our source build
        if [ -f .patched ]; then
                # no patch needed
                return
        fi

        try sed -i 's:flags |= socket.AI_ADDRCONFIG:pass#flags |= socket.AI_ADDRCONFIG:' $BUILD_tornado/tornado/netutil.py

        # everything done, touch the marker !
        touch .patched
}

function build_tornado() {
	if [ -d "$BUILD_PATH/python-install/lib/python2.7/site-packages/tornado" ]; then
		return
	fi

	cd $BUILD_tornado

	push_arm

	export PYTHONPATH=$BUILD_hostpython/Lib/site-packages
        try $BUILD_hostpython/hostpython setup.py install -O2 --root=$BUILD_PATH/python-install --install-lib=lib/python2.7/site-packages
	try rm -rf $BUILD_PATH/python-install/lib/python*/site-packages/tornado/test

	pop_arm
}

function postbuild_tornado() {
	true
}

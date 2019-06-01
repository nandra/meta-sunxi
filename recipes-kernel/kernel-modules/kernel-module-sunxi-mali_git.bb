SUMMARY = "sunxi mali kernel driver for mainline Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = "git://github.com/mripard/sunxi-mali.git;protocol=https;branch=master \
	file://0001-Makefile-use-KERNEL_BUILD_ARTIFACTS_DIR-for-.config-.patch \
	file://0002-build.sh-Don-t-apply-patches-when-applied.patch \
"

PV = "r6p2"

SRCREV = "76e9db284a4e892e7b69cf59220a621fd0d93093"

S = "${WORKDIR}/git"

addtask do_apply_patch before do_compile after do_patch

# call build script to apply patches located in repo source
do_apply_patch () {
	cd ${S}
	./build.sh -r ${PV} -a
}

# we need to navigate to other directory to build proper driver
do_compile_prepend() {
	cd ${S}/${PV}/src/devicedrv/mali
}

do_install_prepend() {
	cd ${S}/${PV}/src/devicedrv/mali
}


export KDIR = "${KERNEL_SRC}"
export KERNEL_BUILD_ARTIFACTS_DIR = "${STAGING_KERNEL_BUILDDIR}"

EXTRA_OEMAKE +=" \
    USING_UMP=0 \
    BUILD=release \
    USING_PROFILING=0 \
    MALI_PLATFORM=sunxi \
    USING_DVFS=1 \
    USING_DEVFREQ=1 \
    "

MODULES_INSTALL_TARGET = "install"
MODULES_MODULE_SYMVERS_LOCATION = "${PV}/src/devicedrv/mali"

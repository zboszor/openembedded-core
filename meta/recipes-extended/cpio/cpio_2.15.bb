SUMMARY = "GNU cpio is a program to manage archives of files"
DESCRIPTION = "GNU cpio is a tool for creating and extracting archives, or copying files from one place to \
another. It handles a number of cpio formats as well as reading and writing tar files."
HOMEPAGE = "http://www.gnu.org/software/cpio/"
SECTION = "base"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949"

SRC_URI = "${GNU_MIRROR}/cpio/cpio-${PV}.tar.gz \
           "

SRC_URI[sha256sum] = "efa50ef983137eefc0a02fdb51509d624b5e3295c980aa127ceee4183455499e"

inherit autotools gettext texinfo

# Issue applies to use of cpio in SUSE/OBS, doesn't apply to us
CVE_CHECK_WHITELIST += "CVE-2010-4226"

EXTRA_OECONF += "DEFAULT_RMT_DIR=${sbindir}"

CFLAGS += "-std=gnu17"

do_install () {
    autotools_do_install
    if [ "${base_bindir}" != "${bindir}" ]; then
        install -d ${D}${base_bindir}/
        mv "${D}${bindir}/cpio" "${D}${base_bindir}/cpio"
        if [ "${sbindir}" != "${bindir}" ]; then
            rmdir ${D}${bindir}/
        fi
    fi

    # Avoid conflicts with the version from tar
    mv "${D}${mandir}/man8/rmt.8" "${D}${mandir}/man8/rmt-cpio.8"
}

PACKAGES =+ "${PN}-rmt"

FILES:${PN}-rmt = "${sbindir}/rmt*"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE:${PN} = "cpio"
ALTERNATIVE:${PN}-rmt = "rmt"

ALTERNATIVE_LINK_NAME[cpio] = "${base_bindir}/cpio"

ALTERNATIVE_PRIORITY[rmt] = "50"
ALTERNATIVE_LINK_NAME[rmt] = "${sbindir}/rmt"

BBCLASSEXTEND = "native nativesdk"

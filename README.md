# **DEPRECATED** clj-boot

Boot tasks simplifying development and deployment.

[![Clojars Project](https://img.shields.io/clojars/v/bradsdeals/clj-boot.svg)](https://clojars.org/bradsdeals/clj-boot)

This is a very early access project that is changing rapidly.

One way to configure clj-boot is to use a boot wrapper that sets the required environment variables.
e.g.:

```bash
#!/usr/bin/env bash

export BOOT_JVM_OPTIONS='-DCONFIG-PROD=/tmp/_test-config-prod.edn -client -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Xverify:none -Xmx2g -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:-OmitStackTraceInFastThrow'

export CLOJARS_USER='clojars-username'
export CLOJARS_PASS='clojars-password'
export CLOJARS_GPG_USER='username@host.for.gpg.com'
export CLOJARS_GPG_PASS='gpg-key-password-for-signing'
```

See https://gist.github.com/chrisroos/1205934 under "method 2" to import deployment/signing keys into your account.

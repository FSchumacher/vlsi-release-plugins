/*
 * Copyright 2019 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.vlsi.gradle.checksum.signatures

import org.junit.jupiter.api.Test

// Parameterized tests are hard to execute/debug one by one, and this class simplifies development
class SingleGradleTest : BaseGradleTest() {
    @Test
    internal fun run() {
        val gradleVersion = "5.5.1"

        createSettings("include('child')")
        projectDir.resolve("build.gradle").write("""
             configurations { tmp }

             project(':child') {
               buildscript {
                  dependencies {
                    classpath 'org.jodd:jodd-core:5.0.6'
                  }
                  repositories { mavenCentral() }
               }
               tasks.create('run') {
               }
             }
        """
        )

        projectDir.resolve("checksum.xml").write("""
            <?xml version='1.0' encoding='utf-8'?>
            <dependency-verification version='1'>
              <trust-requirement pgp='GROUP' checksum='NONE' />
              <ignored-keys />
              <trusted-keys>
                <trusted-key id='72475fd306b9cab7' group='com.googlecode.javaewah' />
                <trusted-key id='a50569c7ca7fa1f0' group='com.jcraft' />
                <trusted-key id='b341ddb020fcb6ab' group='org.bouncycastle' />
                <trusted-key id='5b05ccde140c2876' group='org.eclipse.jgit' />
                <trusted-key id='91ae1504568ec4dd' group='org.jodd' />
                <trusted-key id='2c7b12f2a511e325' group='org.slf4j' />
              </trusted-keys>
              <dependencies />
            </dependency-verification>
        """.trimIndent())

        prepare(gradleVersion, ":child:run", "--info", "--stacktrace")
            .build()
    }
}

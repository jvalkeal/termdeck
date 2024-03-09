/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jvalkeal.config;

import com.github.jvalkeal.TermdeckApplication;
import com.github.jvalkeal.commands.TermdeckCommand;

import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.CommandScan;

/**
 * Only purpose if this configuration class is to introduce {@link CommandScan}
 * within {@code commands} package. We could have simply placed
 * {@link CommandScan} directly into {@link TermdeckApplication} resulting scan
 * from that package downwards but now just keeping it vanilla style.
 *
 * @author Janne Valkealahti
 */
@Configuration
@CommandScan(basePackageClasses = TermdeckCommand.class)
public class TermdeckConfiguration {
}

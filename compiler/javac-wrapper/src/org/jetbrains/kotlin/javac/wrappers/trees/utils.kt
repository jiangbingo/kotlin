/*
 * Copyright 2010-2017 JetBrains s.r.o.
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
 */

package org.jetbrains.kotlin.javac.wrappers.trees

import com.sun.tools.javac.tree.JCTree
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.load.java.JavaVisibilities
import org.jetbrains.kotlin.load.java.structure.JavaClass
import org.jetbrains.kotlin.name.ClassId
import javax.lang.model.element.Modifier

internal val JCTree.JCModifiers.isAbstract: Boolean
    get() = Modifier.ABSTRACT in getFlags()

internal val JCTree.JCModifiers.isFinal: Boolean
    get() = Modifier.FINAL in getFlags()

internal val JCTree.JCModifiers.isStatic: Boolean
    get() = Modifier.STATIC in getFlags()

internal val JCTree.JCModifiers.hasDefaultModifier: Boolean
    get() = Modifier.DEFAULT in getFlags()

internal val JCTree.JCModifiers.visibility: Visibility
    get() = getFlags().let {
        when {
            Modifier.PUBLIC in it -> Visibilities.PUBLIC
            Modifier.PRIVATE in it -> Visibilities.PRIVATE
            Modifier.PROTECTED in it -> {
                if (Modifier.STATIC in it) JavaVisibilities.PROTECTED_STATIC_VISIBILITY
                else JavaVisibilities.PROTECTED_AND_PACKAGE
            }
            else -> JavaVisibilities.PACKAGE_VISIBILITY
        }
    }

internal fun JCTree.annotations(): Collection<JCTree.JCAnnotation> = when (this) {
    is JCTree.JCMethodDecl -> mods?.annotations
    is JCTree.JCClassDecl -> mods?.annotations
    is JCTree.JCVariableDecl -> mods?.annotations
    is JCTree.JCTypeParameter -> annotations
    else -> null
} ?: emptyList<JCTree.JCAnnotation>()

fun JavaClass.computeClassId(): ClassId? =
        outerClass?.computeClassId()?.createNestedClassId(name) ?: fqName?.let { ClassId.topLevel(it) }
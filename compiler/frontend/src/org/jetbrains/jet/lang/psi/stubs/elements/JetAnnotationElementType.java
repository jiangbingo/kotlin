/*
 * Copyright 2010-2013 JetBrains s.r.o.
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

package org.jetbrains.jet.lang.psi.stubs.elements;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.JetAnnotationEntry;
import org.jetbrains.jet.lang.psi.JetPsiUtil;
import org.jetbrains.jet.lang.psi.stubs.PsiJetAnnotationStub;
import org.jetbrains.jet.lang.psi.stubs.impl.PsiJetAnnotationStubImpl;
import org.jetbrains.jet.lang.resolve.name.Name;

import java.io.IOException;

public class JetAnnotationElementType extends JetStubElementType<PsiJetAnnotationStub, JetAnnotationEntry> {

    public JetAnnotationElementType(@NotNull @NonNls String debugName) {
        super(debugName, JetAnnotationEntry.class, PsiJetAnnotationStub.class);
    }

    @Override
    public PsiJetAnnotationStub createStub(@NotNull JetAnnotationEntry psi, StubElement parentStub) {
        Name shortName = JetPsiUtil.getShortName(psi);
        String resultName = shortName != null ? shortName.asString() : psi.getText();
        return new PsiJetAnnotationStubImpl(parentStub, resultName);
    }

    @Override
    public void serialize(@NotNull PsiJetAnnotationStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getShortName());
    }

    @NotNull
    @Override
    public PsiJetAnnotationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef text = dataStream.readName();
        return new PsiJetAnnotationStubImpl(parentStub, text);
    }

    @Override
    public void indexStub(@NotNull PsiJetAnnotationStub stub, @NotNull IndexSink sink) {
        StubIndexServiceFactory.getInstance().indexAnnotation(stub, sink);
    }
}

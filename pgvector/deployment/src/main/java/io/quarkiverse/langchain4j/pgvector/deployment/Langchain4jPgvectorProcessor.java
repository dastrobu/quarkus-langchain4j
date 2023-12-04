package io.quarkiverse.langchain4j.pgvector.deployment;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.jandex.ClassType;
import org.jboss.jandex.DotName;
import org.jboss.jandex.ParameterizedType;

import com.pgvector.PGvector;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.agroal.api.AgroalDataSource;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import io.quarkiverse.langchain4j.pgvector.runtime.PgVectorEmbeddingStoreConfig;
import io.quarkiverse.langchain4j.pgvector.runtime.PgVectorEmbeddingStoreRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class Langchain4jPgvectorProcessor {

    public static final DotName PGVECTOR_EMBEDDING_STORE = DotName.createSimple(PgVectorEmbeddingStore.class);

    private static final String FEATURE = "langchain4j-pgvector";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public void createBean(
            BuildProducer<SyntheticBeanBuildItem> beanProducer,
            PgVectorEmbeddingStoreRecorder recorder,
            PgVectorEmbeddingStoreConfig config) {
        beanProducer.produce(SyntheticBeanBuildItem
                .configure(PGVECTOR_EMBEDDING_STORE)
                .types(ClassType.create(EmbeddingStore.class),
                        ParameterizedType.create(EmbeddingStore.class, ClassType.create(TextSegment.class)))
                .setRuntimeInit()
                .defaultBean()
                .scope(ApplicationScoped.class)
                .addInjectionPoint(ClassType.create(DotName.createSimple(AgroalDataSource.class)))
                .createWith(recorder.embeddingStoreFunction(config))
                .done());

    }

    @BuildStep
    public ReflectiveClassBuildItem reflectiveClass() {
        return ReflectiveClassBuildItem.builder(PGvector.class).build();
    }
}

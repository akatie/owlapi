/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.model;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.providers.EntityProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * Represents the different types of OWL 2 Entities.
 * 
 * @author Matthew Horridge, The University of Manchester, Information
 *         Management Group
 * @since 3.0.0
 * @param <E>
 *        entity type
 */
public class EntityType<E extends OWLEntity> implements Serializable, HasShortForm, HasPrefixedName, HasIRI {

    @FunctionalInterface
    private static interface Builder<T> {

        T buildEntity(IRI i, EntityProvider p);
    }

//@formatter:off
    /** Class entity.               */ @Nonnull public static final EntityType<OWLClass>              CLASS               = new EntityType<>("Class",               "Class",               "Classes",               OWL_CLASS,              (i, p) -> p.getOWLClass(i));
    /** Object property entity.     */ @Nonnull public static final EntityType<OWLObjectProperty>     OBJECT_PROPERTY     = new EntityType<>("ObjectProperty",      "Object property",     "Object properties",     OWL_OBJECT_PROPERTY,    (i, p) -> p.getOWLObjectProperty(i));
    /** Data property entity.       */ @Nonnull public static final EntityType<OWLDataProperty>       DATA_PROPERTY       = new EntityType<>("DataProperty",        "Data property",       "Data properties",       OWL_DATA_PROPERTY,      (i, p) -> p.getOWLDataProperty(i));
    /** Annotation property entity. */ @Nonnull public static final EntityType<OWLAnnotationProperty> ANNOTATION_PROPERTY = new EntityType<>("AnnotationProperty",  "Annotation property", "Annotation properties", OWL_ANNOTATION_PROPERTY,(i, p) -> p.getOWLAnnotationProperty(i));
    /** Named individual entity.    */ @Nonnull public static final EntityType<OWLNamedIndividual>    NAMED_INDIVIDUAL    = new EntityType<>("NamedIndividual",     "Named individual",    "Named individuals",     OWL_NAMED_INDIVIDUAL,   (i, p) -> p.getOWLNamedIndividual(i));
    /** Datatype entity.            */ @Nonnull public static final EntityType<OWLDatatype>           DATATYPE            = new EntityType<>("Datatype",            "Datatype",            "Datatypes",             RDFS_DATATYPE,          (i, p) -> p.getOWLDatatype(i));
//@formatter:on
    private static final List<EntityType<?>> VALUES = Collections.<EntityType<?>> unmodifiableList(Arrays.asList(CLASS,
        OBJECT_PROPERTY, DATA_PROPERTY, ANNOTATION_PROPERTY, NAMED_INDIVIDUAL, DATATYPE));
    @Nonnull private final String name;
    @Nonnull private final OWLRDFVocabulary vocabulary;
    @Nonnull private final String printName;
    @Nonnull private final String pluralPrintName;
    @Nonnull private final Builder<E> builder;

    protected EntityType(String name, String print, String pluralPrint, OWLRDFVocabulary vocabulary,
        Builder<E> builder) {
        this.name = name;
        this.vocabulary = vocabulary;
        printName = print;
        pluralPrintName = pluralPrint;
        this.builder = builder;
    }

    /**
     * @return toe vocabulary enum corresponding to this entity
     */
    public OWLRDFVocabulary getVocabulary() {
        return vocabulary;
    }

    /**
     * @return this entity tipe name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the list of known entity types
     */
    public static List<EntityType<?>> values() {
        return VALUES;
    }

    /**
     * @return printable name
     */
    public String getPrintName() {
        return printName;
    }

    /**
     * @return plural printable name
     */
    public String getPluralPrintName() {
        return pluralPrintName;
    }

    @Override
    public String getShortForm() {
        return name;
    }

    @Override
    public String getPrefixedName() {
        return vocabulary.getPrefixedName();
    }

    @Override
    public IRI getIRI() {
        return vocabulary.getIRI();
    }

    /**
     * @param visitor
     *        visitor to accept
     * @param <T>
     *        return type
     * @return visitor return value
     */
    public <T> Optional<T> accept(EntityTypeVisitorEx<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Build an entity of this type, using the factory passed in as df.
     * 
     * @param i
     *        iri
     * @param df
     *        data factory
     * @return entity
     */
    public E buildEntity(IRI i, EntityProvider df) {
        return builder.buildEntity(i, df);
    }
}

package org.quasarch.akash.model;

import java.util.Collection;

public record AkashPage<T>(short currentPage, short resultPerPage, Collection<T> results) {

}

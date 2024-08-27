package com.revature.eeecommerce.util.interfaces;

public interface Crudable<O> {
    boolean update(O updatedObject);
    boolean delete(O removedObject);
}

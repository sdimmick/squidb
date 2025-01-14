/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache 2.0 License.
 * See the accompanying LICENSE file for terms.
 */
package com.yahoo.squidb.data;

import com.yahoo.squidb.sql.Criterion;
import com.yahoo.squidb.sql.Query;
import com.yahoo.squidb.test.DatabaseTestCase;
import com.yahoo.squidb.test.TestVirtualModel;

public class VirtualModelTest extends DatabaseTestCase {

    public void testCrudMethods() {
        // insert
        TestVirtualModel model = new TestVirtualModel()
                .setTitle("Charlie")
                .setBody("Charlie and the Chocolate Factory");
        assertTrue(dao.createNew(model));
        assertEquals(1, dao.count(TestVirtualModel.class, Criterion.all));

        // query
        final long id = model.getId();
        TestVirtualModel fetched = dao.fetch(TestVirtualModel.class, id, TestVirtualModel.PROPERTIES);
        assertEquals(model, fetched);

        // update
        model.setTitle("Charlie Brown").setBody("It's the Easter Beagle, Charlie Brown");
        assertTrue(dao.saveExisting(model));
        assertEquals(1, dao.count(TestVirtualModel.class, Criterion.all));
        assertEquals(1, dao.count(TestVirtualModel.class, TestVirtualModel.TITLE.eq("Charlie Brown")));

        // update using setId on a template
        TestVirtualModel model2 = new TestVirtualModel().setTitle("Charlie Brown 2").setId(model.getId());
        assertTrue(dao.saveExisting(model2));
        assertEquals(1, dao.count(TestVirtualModel.class, Criterion.all));
        assertEquals(1, dao.count(TestVirtualModel.class, TestVirtualModel.TITLE.eq("Charlie Brown 2")));

        // delete
        assertTrue(dao.delete(TestVirtualModel.class, id));
        assertEquals(0, dao.count(TestVirtualModel.class, Criterion.all));
    }

    public void testNonStringPropertyInVirtualTableModel() {
        final Long testNum = 7L;
        TestVirtualModel model = new TestVirtualModel()
                .setTestNumber(testNum);
        assertTrue(dao.createNew(model));

        final long id = model.getId();
        TestVirtualModel fetched = dao.fetch(TestVirtualModel.class, id, TestVirtualModel.PROPERTIES);
        assertEquals(id, fetched.getId());
        assertEquals(testNum, fetched.getTestNumber());
    }

    public void testSelectAllIncludesRowid() {
        // insert
        TestVirtualModel model = new TestVirtualModel()
                .setTitle("Charlie")
                .setBody("Charlie and the Chocolate Factory");
        assertTrue(dao.createNew(model));

        long expectedId = model.getId();

        TestVirtualModel fetchedModel = dao.fetchByQuery(TestVirtualModel.class, Query.select());
        assertEquals(expectedId, fetchedModel.getId());
        assertEquals(model, fetchedModel);

    }
}

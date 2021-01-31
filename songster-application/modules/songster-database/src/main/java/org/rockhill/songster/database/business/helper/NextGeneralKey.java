package org.rockhill.songster.database.business.helper;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Supporter class to get a identification number (ID) that is unique in the whole database - to identify all objects/record in a unique way.
 */
@Component
public class NextGeneralKey {

    /**
     * Generates the next general key within the database. Must be called from within a transaction.
     *
     * Note that this works with PostgreSQL database only.
     *
     * In case of MSSQL database an alternative code need to be used:
     * Iterator<BigInteger> iter;
     * Query query = session.createSQLQuery( "SELECT NEXT VALUE FOR dbo.UniqueNumber");
     * iter = (Iterator<BigInteger>) query.getResultList();
     * iter.next().longValue();
     *
     * @return with the next key
     */
    public Long getNextGeneralKay(Session session) {
        ArrayList<BigInteger> values;
        Query query = session.createSQLQuery("select nextval('\"dbo\".\"UniqueNumber\"')"); //NOSONAR
        values = (ArrayList<BigInteger>) query.getResultList(); //NOSONAR
        return values.get(0).longValue();
    }

}

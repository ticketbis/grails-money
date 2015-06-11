package com.ticketbis.money

import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Types
import java.math.BigDecimal
import java.lang.reflect.*
import java.util.Currency

import org.hibernate.HibernateException
import org.hibernate.usertype.UserType

class MoneyUserType implements UserType {

    private static final int[] SQL_TYPES = [Types.DECIMAL]

    Object assemble(Serializable cached, Object owner)
            throws HibernateException {

        cached
    }

    Object deepCopy(Object o) throws HibernateException {
        o.clone()
    }

    Serializable disassemble(Object value) throws HibernateException {
        (Serializable) value
    }

    boolean equals(Object x, Object y) throws HibernateException {
        x == y
    }

    int hashCode(Object o) throws HibernateException {
        o.hashCode()
    }

    boolean isMutable() {
        false
    }

    Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException {
        assert names.length == 1

        String amount = rs.getString(names[0])
        String currency = "EUR"

        ResultSetMetaData rsmd = rs.getMetaData()
        int columnCount = rsmd.getColumnCount()

        // The column count starts from 1
        for (int i = 1; i < columnCount + 1; i++ ) {
              String name = rsmd.getColumnName(i)
              if (name.equals("divisa")) {
                  currency = rs.getString(i)
              }
        }

        if (amount == null && currency == null)
            return null

        new Money(amount, currency)
    }

    void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException {

        if (value == null) {
            st.setBigDecimal(index, BigDecimal.ZERO)
        } else {
            Money money = (Money) value
            st.setBigDecimal(index, money.getAmount())
        }
    }

    Object replace(Object original, Object target, Object owner)
            throws HibernateException {

        return original
    }

    Class returnedClass() {
        Money.class
    }

    int[] sqlTypes() {
        SQL_TYPES
    }
}

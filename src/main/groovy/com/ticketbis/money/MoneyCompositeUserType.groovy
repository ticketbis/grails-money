package com.ticketbis.money

import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.math.BigDecimal
import java.util.Currency

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.usertype.CompositeUserType
import org.hibernate.type.Type
import org.hibernate.type.BigDecimalType
import org.hibernate.type.StringType

import groovy.util.logging.Log4j
import groovy.transform.CompileStatic

@CompileStatic
@Log4j
class MoneyCompositeUserType implements CompositeUserType {

    private static final String[] PROPERTY_NAMES = ['amount', 'currency']
    private static final Type[] PROPERTY_TYPES = [BigDecimalType.INSTANCE, StringType.INSTANCE] as Type[]

    String[] getPropertyNames() {
        PROPERTY_NAMES
    }

    Type[] getPropertyTypes() {
        PROPERTY_TYPES
    }

    Object getPropertyValue(Object component, int propertyIndex) {
        if (component == null) {
            return null
        }

        final Money money = (Money) component
        switch (propertyIndex) {
            case 0:
                return money.amount
            case 1:
                return money.currency
            default:
                throw new HibernateException("Invalid property index [${propertyIndex}]")
        }
    }

    void setPropertyValue(Object component, int propertyIndex, Object value) throws HibernateException {
        // Money is an inmutable type and it's properties cannot be modified
        throw new UnsupportedOperationException()
    }

    Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        assert names.length == 2
        BigDecimal amount = rs.getBigDecimal(names[0])
        String currencyCode = rs.getString(names[1])
        Currency currency = (Currency) (currencyCode ? Currency.getInstance(currencyCode) :  null)

        if (amount == null && currency == null)
            return null

        new Money(amount, currency)
    }

    void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws SQLException {
        if (value) {
            final Money money = (Money) value
            st.setBigDecimal(index, money.amount)
            st.setString(index + 1, money.currency.currencyCode)
        } else {
            st.setBigDecimal(index, null)
            st.setString(index + 1, null)
        }
    }

    boolean equals(Object x, Object y) {
        x == y
    }

    Class returnedClass() {
        Money
    }

    Object assemble(Serializable cached, SessionImplementor session, Object owner) {
        cached
    }

    Object deepCopy(Object o) {
        ((Money) o)?.clone()
    }

    Serializable disassemble(Object value, SessionImplementor session) {
        (Serializable) value
    }

    int hashCode(Object o) {
        o.hashCode()
    }

    boolean isMutable() {
        false
    }

    Object replace(Object original, Object target, SessionImplementor session, Object owner) {
        original
    }
}

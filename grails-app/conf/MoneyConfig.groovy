grails.gorm.default.constraints = {
    gtZero(validator: { val -> val ? val.amount > BigDecimal.ZERO : true })
}

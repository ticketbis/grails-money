grails.gorm.default.constraints = {
    gtZero(validator: { val -> val?.amount > BigDecimal.ZERO })
}

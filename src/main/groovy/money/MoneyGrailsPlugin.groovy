package money

import grails.converters.JSON
import grails.plugins.*
import grails.validation.ConstrainedProperty

import com.ticketbis.money.Money
import com.ticketbis.money.NumberMoneyExtension
import com.ticketbis.money.validation.GreaterThanZeroConstraint
import com.ticketbis.money.validation.GreaterOrEqualZeroConstraint

class MoneyGrailsPlugin extends Plugin {
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.2.3 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Money Plugin" // Headline display name of the plugin
    def author = "David Santamaría & Álvaro Salazar & Endika Gutiérrez & Alfredo Filardo"
    def authorEmail = ""
    def description = '''\
Grails plugin for manage money and currency exchange
'''
    def group = "com.ticketbis"

    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/money"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Ticketbis", url: "http://engineering.ticketbis.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/ticketbis/grails-money" ]

    Closure doWithSpring() { {->
            // Object Marshaller register
            JSON.registerObjectMarshaller(Money) {
                def returnArray = [:]
                returnArray['amount'] = it.amount
                return returnArray
            }

            //Custom structured property editor data binding for Money type
            moneyEditor com.ticketbis.money.StructuredMoneyEditor

            ConstrainedProperty.registerNewConstraint(
                GreaterThanZeroConstraint.CONSTRAINT_NAME,
                GreaterThanZeroConstraint)

            ConstrainedProperty.registerNewConstraint(
                GreaterOrEqualZeroConstraint.CONSTRAINT_NAME,
                GreaterOrEqualZeroConstraint)
        }
    }

    void doWithDynamicMethods() {
        Number.mixin(NumberMoneyExtension)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}

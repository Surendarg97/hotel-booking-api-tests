package com.booking.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",     // location of feature files
        glue = {"com.booking.stepdefinitions"},       // step definitions package
        plugin = {
                "pretty",                             // readable console output
                "html:target/reports/cucumber.html"   // basic HTML report
        },
        monochrome = true                            // clean console output
)
public class TestRunner {
}
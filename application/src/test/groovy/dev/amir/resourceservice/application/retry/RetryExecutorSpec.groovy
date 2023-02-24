package dev.amir.resourceservice.application.retry

import dev.amir.resourceservice.application.retry.exception.UnexpectedRetryException
import org.springframework.retry.RetryPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import spock.lang.Specification

class RetryExecutorSpec extends Specification {
    private final RetryTemplate retryTemplate = new RetryTemplate()
    private final RetryExecutorImpl retryExecutor = new RetryExecutorImpl(retryTemplate)

    def "execute should throw UnexpectedRetryException after retrying twice on failures"() {
        given:
        RetryPolicy retryPolicy = new SimpleRetryPolicy(2)
        retryTemplate.setRetryPolicy(retryPolicy)
        RetryAction action = Mock()

        when:
        retryExecutor.execute(action)

        then:
        2 * action.execute() >>> [{ throw new NullPointerException() }, { throw new NullPointerException() }]

        and:
        thrown(UnexpectedRetryException)
    }

    def "execute should not retry after success"() {
        given:
        RetryPolicy retryPolicy = new SimpleRetryPolicy(5)
        retryTemplate.setRetryPolicy(retryPolicy)
        RetryAction action = Mock()

        when:
        retryExecutor.execute(action)

        then:
        1 * action.execute() >> { throw new NullPointerException() }

        and:
        1 * action.execute()
    }
}

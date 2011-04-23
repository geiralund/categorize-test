package no.kodemaker.categorize

import spock.lang.Specification
import org.junit.internal.runners.statements.FailOnTimeout
import org.junit.runners.model.FrameworkMethod
import java.lang.reflect.Method
import org.junit.runners.model.Statement
import org.junit.internal.runners.statements.InvokeMethod


class SlowCategoryAwareSpec extends Specification{

    Categorizable categorizable = new Categorizable()

    def "should return same statement as input when slow env not variable set"(){
        when :
            def statement = categorizable.apply(null,null,null)
        then :
            statement == null
    }

    def "should throw illegalstate if frameworkmethod is null"(){
        given :
            System.setProperty("category","slow")
        expect :
            IllegalArgumentException  == categorizable.apply(null,null,null)


    }

    @Category(name = "slow")
    def "should check that test category againts specified method category and return FailOnTimeout if same"(){
        given :
            System.setProperty("category","slow")
            Method methodCall = this.getClass().getMethod("should check that test category againts specified method category and return FailOnTimeout if same")
            FrameworkMethod method = new FrameworkMethod(methodCall)
        when :
            def statement = categorizable.apply(null,method,null)
        then :
            statement != null
            statement instanceof FailOnTimeout
    }


    @Category(name = "fast")
    def "should check that test category againts specified method category and return null if not the same"(){
        given :
            System.setProperty("category","slow")
            Method methodCall = this.getClass().getMethod("should check that test category againts specified method category and return null if not the same")
            FrameworkMethod method = new FrameworkMethod(methodCall)
        when :
            def statement = categorizable.apply(null,method,null)
        then :
            statement == null
    }

    @Category(name = "fast")
    def "should check that test category againts specified method category and return FailOnTimeOut if same"(){
        given :
            System.setProperty("category","fast")
            Method methodCall = this.getClass().getMethod("should check that test category againts specified method category and return FailOnTimeOut if same")
            FrameworkMethod method = new FrameworkMethod(methodCall)
        when :
            def statement = categorizable.apply(null,method,null)
        then :
            statement != null
            statement instanceof FailOnTimeout
    }

    def "should return same as input if no category specified"(){
        given :
            System.setProperty("category","fast")
            Method methodCall = this.getClass().getMethod("should return same as input if no category specified")
            FrameworkMethod method = new FrameworkMethod(methodCall)
            Statement statement = new InvokeMethod(method,this)
        when :
            def returnStatement = categorizable.apply(statement,method,null)
        then :
            statement == returnStatement
    }
}

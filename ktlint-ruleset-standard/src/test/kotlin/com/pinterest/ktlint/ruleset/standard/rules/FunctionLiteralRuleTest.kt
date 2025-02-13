package com.pinterest.ktlint.ruleset.standard.rules

import com.pinterest.ktlint.ruleset.standard.StandardRuleSetProvider
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.EOL_CHAR
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.MAX_LINE_LENGTH_MARKER
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class FunctionLiteralRuleTest {
    private val functionLiteralRuleAssertThat = assertThatRule { FunctionLiteralRule() }

    @Test
    fun `Given a single line lambda without parameters`() {
        val code =
            """
            val foobar = { foo + bar }
            """.trimIndent()
        functionLiteralRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `Given a multiline lambda without parameters`() {
        val code =
            """
            val foobar =
                {
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `Given a lambda with a single parameter fitting on the first line`() {
        val code =
            """
            val foobar =
                { foo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `Given a lambda with a single parameter not on same line as opening brace`() {
        val code =
            """
            val foobar =
                {
                    foo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        val formattedCode =
            """
            val foobar =
                { foo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .hasLintViolation(3, 9, "No newline expected before parameter")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with a single parameter and arrow on separate line`() {
        val code =
            """
            val foobar =
                { foo: Foo
                    ->
                    foo.repeat(2)
                }
            """.trimIndent()
        val formattedCode =
            """
            val foobar =
                { foo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .hasLintViolation(2, 15, "No newline expected after parameter")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with a single parameter not fitting on the first line`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                { fooooooooooooooo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasNoLintViolations()
    }

    @Test
    fun `Given a call expression followed by a lambda with a single parameter not fitting on the same line as the opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                barrrrrrrrrr { foooooooooooo: Foo ->
                    foo.repeat(2)
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                barrrrrrrrrr {
                    foooooooooooo: Foo
                    ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(3, 20, "Newline expected before parameter"),
                LintViolation(3, 39, "Newline expected before arrow"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with multiple parameters fitting on the first line`() {
        val code =
            """
            val foobar =
                { foo: Foo, bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `Given a lambda with multiple parameters but not fitting on the first line`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                { fooooo: Foo, bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                    fooooo: Foo,
                    bar: Bar
                    ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(3, 7, "Newline expected before parameter"),
                LintViolation(3, 20, "Newline expected before parameter"),
                LintViolation(3, 29, "Newline expected before arrow"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with multiple parameters of which some are not fitting on line`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                { fooooooooooo: Foo, bar: Bar ->
                    foo.repeat(2)
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                    fooooooooooo: Foo,
                    bar: Bar
                    ->
                    foo.repeat(2)
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(3, 7, "Newline expected before parameter"),
                LintViolation(3, 26, "Newline expected before parameter"),
                LintViolation(3, 35, "Newline expected before arrow"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with a multiline parameter list starting on same line as opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                { foo: Foo,
                    bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                    foo: Foo,
                    bar: Bar
                    ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(3, 7, "Newline expected before parameter"),
                LintViolation(4, 18, "Newline expected before arrow"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a lambda with a multiline parameter list starting on the next line below the opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                    foo: Foo,
                    bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                    foo: Foo,
                    bar: Bar
                    ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolation(5, 18, "Newline expected before arrow")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a single line parameter list starting on the next line below the opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                {
                  foo: Foo, bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER  $EOL_CHAR
            val foobar =
                { foo: Foo, bar: Bar ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolation(4, 7, "No newline expected before parameter")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a single line parameter list starting on the next line below the opening brace and arrow on separate line which can be merged to a single line after opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER          $EOL_CHAR
            val foobar =
                {
                  foo: Foo, bar: Bar, baz: Baz
                  ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER          $EOL_CHAR
            val foobar =
                { foo: Foo, bar: Bar, baz: Baz ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(4, 7, "No newline expected before parameter"),
                LintViolation(4, 35, "No newline expected after parameter"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a single line parameter list starting on the next line below the opening brace and arrow on separate line which can not be merged to a single line after opening brace`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER         $EOL_CHAR
            val foobar =
                {
                    foo: Foo, bar: Bar, baz: Baz
                    ->
                    foo + bar
                }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER         $EOL_CHAR
            val foobar =
                {
                    foo: Foo,
                    bar: Bar,
                    baz: Baz
                    ->
                    foo + bar
                }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .isFormattedAs(formattedCode)
            .hasLintViolations(
                LintViolation(4, 19, "Newline expected before parameter"),
                LintViolation(4, 29, "Newline expected before parameter"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a single line function literal not exceeding the max line length and having a parameter list`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER                     $EOL_CHAR
            val foobar = { foo: Foo, bar: Bar -> foo + bar }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .setMaxLineLength()
            .hasNoLintViolations()
    }

    @Test
    fun `Given a single line function literal exceeding the max line length and having a parameter list`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER                    $EOL_CHAR
            val foobar = { foo: Foo, bar: Bar -> foo + bar }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER                    $EOL_CHAR
            val foobar = { foo: Foo, bar: Bar ->
                foo + bar
            }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .addAdditionalRuleProvider { MultilineExpressionWrappingRule() }
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(2, 36, "Newline expected after arrow"),
                LintViolation(2, 48, "Newline expected before closing brace"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a multiline code block starting on same line a arrow`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER               $EOL_CHAR
            val foobar = { foo: Foo, bar: Bar -> foo +
                bar
            }
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER               $EOL_CHAR
            val foobar = { foo: Foo, bar: Bar ->
                foo +
                bar
            }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .addAdditionalRuleProvider { MultilineExpressionWrappingRule() }
            .setMaxLineLength()
            .hasLintViolation(2, 36, "Newline expected after arrow")
            .isFormattedAs(formattedCode)
    }

    @Test
    fun `Given a single line function literal without parameters that exceeds the maximum line length`() {
        val code =
            """
            // $MAX_LINE_LENGTH_MARKER    $EOL_CHAR
            val foobar = { it.foo().bar().foobar() }
            val foo = bar.filter { it > 2 }!!.takeIf { it.count() > 100 }.map { it * it }
                ?.sum()!!
            """.trimIndent()
        val formattedCode =
            """
            // $MAX_LINE_LENGTH_MARKER    $EOL_CHAR
            val foobar = {
                it
                    .foo()
                    .bar()
                    .foobar()
            }
            val foo =
                bar
                    .filter { it > 2 }!!
                    .takeIf {
                        it.count() > 100
                    }.map { it * it }
                    ?.sum()!!
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .addAdditionalRuleProvider { MultilineExpressionWrappingRule() }
            .addAdditionalRuleProvider { ChainMethodContinuationRule() }
            .addAdditionalRuleProvider { IndentationRule() }
            .addAdditionalRuleProvider { ArgumentListWrappingRule() }
            .addRequiredRuleProviderDependenciesFrom(StandardRuleSetProvider())
            .setMaxLineLength()
            .hasLintViolations(
                LintViolation(2, 14, "Newline expected after opening brace"),
                LintViolation(2, 40, "Newline expected before closing brace"),
                LintViolation(3, 22, "Newline expected after opening brace"),
                LintViolation(3, 31, "Newline expected before closing brace"),
                LintViolation(3, 42, "Newline expected after opening brace"),
                LintViolation(3, 61, "Newline expected before closing brace"),
                LintViolation(3, 67, "Newline expected after opening brace"),
                LintViolation(3, 77, "Newline expected before closing brace"),
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `Issue 2331 - Given a function literal with redundant arrow`() {
        val code =
            """
            fun foo(block: () -> Unit): Unit = foo { -> block() }
            """.trimIndent()
        val formattedCode =
            """
            fun foo(block: () -> Unit): Unit = foo { block() }
            """.trimIndent()
        functionLiteralRuleAssertThat(code)
            .hasLintViolation(1, 42, "Arrow is redundant when parameter list is empty")
            .isFormattedAs(formattedCode)
    }
}

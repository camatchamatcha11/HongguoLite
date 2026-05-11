package com.example.hongguolite

import org.junit.Test

import org.junit.Assert.*
// 直接在文件顶层写，不需要包裹在类里

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        println("这是 Kotlin 的入口函数")
    }

    @Test
    fun testLambdaLogic() {
        // 1. 无参数 Lambda
        val sayHi = { println("你好") }
        sayHi()

        // 2. 带参数 Lambda
        val sayHello = { name: String -> println("你好, $name") }
        sayHello("Android")

        // 3. 高阶函数与尾随 Lambda
        fun doAction(block: () -> Unit) {
            print("准备执行，")
            block()
        }
        doAction { println("我是尾随 Lambda 代码") }

        // 4. 带返回值的 Lambda
        val sum = { a: Int, b: Int -> a + b }
        println("计算结果: ${sum(10, 20)}")

        // 5. it 隐式参数
        val square: (Int) -> Int = { it * it }
        val square2: (Int) -> Int = { x -> x * x }
        println("5 的平方是: ${square(5)}")

        // 6. 常用场景：集合操作
        val list = listOf(1, 2, 3, 4, 5)
        val evens = list.filter { it % 2 == 0 }
        println("偶数列表: $evens")

        // 1. 定义：这里的 () 说明这个 task 执行时不需要传参数进去
        fun run(task: () -> Unit) {
            task() // 激活执行
        }

        // 2. 调用：{} 里的内容就是 task 的“身体”
        run {
            // 这里就是代码块，也就是 task 的具体逻辑
            println("任务开始")
        println("任务结束")    }

    }

}
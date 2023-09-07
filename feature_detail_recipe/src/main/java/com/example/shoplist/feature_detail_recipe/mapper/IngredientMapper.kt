package com.example.shoplist.feature_detail_recipe.mapper

import com.example.shoplist.domain.models.ShoplistEntity
import java.text.DecimalFormat
import java.util.regex.Pattern

class IngredientMapper {

    operator fun invoke(
        ingredient: Pair<String, String>,
        recipeId: Int,
        recipeTitle: String
    ) = ShoplistEntity(
        ingredientName = ingredient.first,
        ingredientMeasure = parseMeasure(ingredient.second),
        recipe = ShoplistEntity.Recipe(id = recipeId, title = recipeTitle)
    )

    private fun parseMeasure(measure: String): ShoplistEntity.Measure {
        val amount: Float
        val title: String

        // trim spaces
        var source = measure.trim()

        // trim info in ()
        val roundBracketsRegex = BRACES_REGEXP.toRegex()
        source = source.replace(roundBracketsRegex, "")

        // trim duplicate info after /
        val slashPattern = Pattern.compile(SLASH_DUP_REGEXP)
        val slashMatcher = slashPattern.matcher(source)

        if (slashMatcher.find()) {
            slashMatcher.group(1)?.let { group ->
                source = source.replace(group, "")
            }
        }

        // get numbers
        val numbers = NUMBERS_REGEXP.toRegex()
            .findAll(source)
            .map { it.value }
            .toList()

        //parse numbers and remove it from source
        when (numbers.count()) {
            1 -> {
                amount = getAmountFromNumber(numbers[0])
                title = source.prepareMeasureString(numbers[0])
            }
            2 -> {
                val number1 = getAmountFromNumber(numbers[0])
                val number2 = getAmountFromNumber(numbers[1])
                val interval = source.indexOf(numbers[1]) -
                        (source.indexOf(numbers[0]) + numbers[0].length)

                if (interval != 1) {
                    amount = number1
                    title = source.prepareMeasureString(numbers[0])
                } else {
                    amount =
                        if (number1 < number2) number2
                        else number1 + number2
                    title = source.prepareMeasureString(numbers[0], numbers[1])
                }
            }
            else -> {
                amount = 1f
                title = source.ifEmpty { DEFAULT_TITLE }
            }
        }

        return ShoplistEntity.Measure(amount = amount, title = title)
    }

    private fun getAmountFromNumber(number: String) = run {
        if (number.contains(SLASH)) {
            val fractionParts = number.split(SLASH)

            DecimalFormat("#.##")
                .format(fractionParts[0].toFloat() / fractionParts[1].toFloat())
        } else number
    }
        .replace(',', '.')
        .toFloat()

    private fun String.prepareMeasureString(vararg numbersToRemove: String): String {
        var res = this

        numbersToRemove.forEach { number ->
            res = res.replaceFirst(number, "")
        }

        return res
            .trim()
            .removePrefix(DELIMITER_HYPHEN)
            .removePrefix(DELIMITER_X)
            .trim()
            .ifEmpty { DEFAULT_TITLE }
    }

    private companion object {
        const val BRACES_REGEXP = "\\s*\\([^()]*\\)"
        const val SLASH_DUP_REGEXP = "^((.*?)[a-zA-Z])/"
        const val NUMBERS_REGEXP = "\\d+(?:[.,/]\\d+)?"
        const val DEFAULT_TITLE = "pc"
        const val SLASH = '/'
        const val DELIMITER_X = "x"
        const val DELIMITER_HYPHEN = "-"
    }
}
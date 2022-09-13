package com.example.meteors;

public class Crypt
{
    private final String keyword = "K1b8JNBL6PrqBRyb";
    private final char[] key = keyword.toCharArray();

    private final char[] characters = new char[]         // 11x13+5 = 148!
            {
                    'Ь', 'х', 'F', 'Ю', '2', ':', '*', 'ё', 'Л', 'r', '$', 'v', 'щ',
                    'Ч', 'У', 'Е', 'э', 'ф', '!', 'a', 'ъ', '_', 'V', 'о', 'Ц', '&',
                    'x', 'k', '0', 'А', 'Д', 'Я', 'я', '8', 'Э', 'h', 'а', 'Ш', 'Г',
                    'М', 'п', 'Т', 'Q', 'р', '1', 'n', '+', 'E', 'b', 'л', ' ', 'y',
                    'H', '%', 'г', '/', 'C', 'Z', 'б', 'ю', 'g', 's', 'T', '5', 'j',
                    '^', 'w', 't', 'з', 'у', '6', 'Ж', 'I', 'U', 'W', 'X', 'D', '#',
                    'т', 'A', 'K', 'N', 'С', '4', 'К', '7', 'Б', 'Ы', 'в', 'z', 'Ъ',
                    '@', 'd', 'ы', 'й', 'Y', 'P', 'И', 'ц', 'Н', 'l', 'е', 'B', '3',
                    'Й', 'и', 'ч', 'S', 'c', 'к', '=', 'ь', 'p', 'ж', 'L', 'f', 'Щ',
                    'О', 'н', 'Х', 'o', 'З', 'i', 'M', 'В', 'Р', 'q', 'П', 'O', '(',
                    '9', 'R', '-', 'u', 'G', 'с', 'J', 'Ф', 'м', 'Ё', 'e', 'д', 'ш',
                    'm', ')', '.', ',', '?'
            };

    int N = characters.length;

    // Шифровка:
    public String Encode(String input)
    {
        StringBuilder result = new StringBuilder();

        int c;
        int keyword_index = 0;

        char[] charInput = input.toCharArray();

        for (int i = 0; i < input.length(); i ++)
        {
            int characterIndex = new String(characters).indexOf(charInput[i]);
            if (characterIndex < 0)
            {
                result.append(charInput[i]);
            }
            else
            {
                c = (characterIndex + new String(characters).indexOf(key[keyword_index])) % N;

                result.append(characters[c]);
            }

            keyword_index++;
            if ((keyword_index + 1) == key.length)
            {
                keyword_index = 0;
            }
        }

        return result.toString();
    }

    // Дешифровка:
    public String Decode(String input)
    {
        StringBuilder result = new StringBuilder();

        int p;
        int keyword_index = 0;

        char[] charInput = input.toCharArray();

        for (int i = 0; i < input.length(); i ++)
        {
            int characterIndex = new String(characters).indexOf(charInput[i]);
            if (characterIndex < 0)
            {
                result.append(charInput[i]);
            }
            else
            {
                p = (characterIndex + N - new String(characters).indexOf(key[keyword_index])) % N;

                result.append(characters[p]);
            }

            keyword_index++;

            if ((keyword_index + 1) == key.length)
                keyword_index = 0;
        }

        return result.toString();
    }

    // Дешифровка таблицы:
    /*public void Encrypt(int table, int kol)
    {
        for (int i = 0; i < table; i++)
        {
            for (int j = 1; j <= kol; j++)
            {
                Data.dt_user.Rows[i][j] = Decode(Data.dt_user.Rows[i][j].ToString(), key);
            }
        }
    }*/
}

package com.carsharing.service.polygon;

public class CPoints
{
    // Массив абсцисс.
    private double[] m_x;
    // Массив ординат.
    private double[] m_y;
    // Количество точек.
    private int m_count;
    // Вместимость.
    private int m_capacity;
    // Количество элементов, добавляемых при расширении.
    private final int m_block_size;
    // Минимальный размер m_block_size.
    private final int m_block_size_minimal = 10;

    // Устанавливает размер блока в значение по умолчанию.
    public CPoints()
    {
        m_block_size = m_block_size_minimal;
        m_count = 0;
        m_capacity = 0;
        increase();
    }

    // default_block_size - размер блока, если меньше чем
    // m_block_size_minimal,то игнорируется.
    public CPoints(int default_block_size)
    {
        if(default_block_size < m_block_size_minimal)
            default_block_size = m_block_size_minimal;
        m_block_size = default_block_size;
        m_count = 0;
        m_capacity = 0;
        increase();
    }

    // Добавляет точку в конец массива.
    public void push(final double x, final double y)
    {
        // Если добавлять некуда, то увеличиваем размер массивов.
        if(m_count == m_capacity)
            increase();

        m_x[m_count] = x;
        m_y[m_count] = y;
        m_count++;
    }

    // Удаляет последнюю точку.
    public void pop()
    {
        if(m_count > 0)
            m_count--;
    }

    /// Возвращает размер массива.
    public int count()
    {
        return m_count;
    }

    // Возвращает массив X'ов.
    public final double[] getXArray()
    {
        return m_x;
    }

    // Возвращает массив Y'ов.
    public final double[] getYArray()
    {
        return m_y;
    }

    // Увеличевает размер массивов на m_block_size.
    private void increase()
    {
        int new_capasity = m_capacity + m_block_size;
        if(m_capacity != 0)
        {
            double[] tempx = new double[new_capasity];
            double[] tempy = new double[new_capasity];

            for(int i = 0; i < m_capacity; i++)
            {
                tempx[i] = m_x[i];
                tempy[i] = m_y[i];
            }

            m_x = tempx;
            m_y = tempy;
        }
        else
        {
            m_x = new double[new_capasity];
            m_y = new double[new_capasity];
        }
        m_capacity = new_capasity;
    }
}
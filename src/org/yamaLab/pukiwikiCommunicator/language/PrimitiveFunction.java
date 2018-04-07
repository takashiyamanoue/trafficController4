package org.yamaLab.pukiwikiCommunicator.language;
public interface PrimitiveFunction
{
    abstract LispObject fun(LispObject proc, LispObject arg);
}


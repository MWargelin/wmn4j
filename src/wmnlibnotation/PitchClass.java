/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wmnlibnotation;

/**
 *
 * @author Otso Björklund
 */
public enum PitchClass {
    C(0), 
    CSharpDFlat(1), 
    D(2), 
    DSharpEFlat(3), 
    E(4), 
    F(5), 
    FSharpGFlat(6), 
    G(7), 
    GSharpAFlat(8), 
    A(9), 
    ASharpBFlat(10), 
    B(11);
    
    static private final PitchClass pcs[]
            = {C, CSharpDFlat, D, DSharpEFlat, E, F, FSharpGFlat, G, GSharpAFlat, A, ASharpBFlat, B};
    
    private final int number;
    
    private PitchClass(int number) {
        this.number = number;
    }
    
    public int toInt() {
        return this.number;
    }
    
    public static PitchClass fromInt(int pitchNumber) {
        if(pitchNumber < 0)
            throw new IllegalArgumentException("pitchNumber must be non-negative.");
        
        return pcs[pitchNumber % 12];
    }
}

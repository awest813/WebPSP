package jpcsp.Allegrex.compiler;

import jpcsp.Allegrex.Common;
import jpcsp.Allegrex.Common.Instruction;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RegisterAnalyzer {

    // Number of registers to cache
    private static final int MAX_CACHED_REGISTERS = 8;

    public static int[] getMostFrequentRegisters(List<CodeInstruction> instructions) {
        final Map<Integer, Integer> registerCounts = new HashMap<Integer, Integer>();

        for (CodeInstruction instruction : instructions) {
            // Check reads
            int rs = instruction.getRsRegisterIndex();
            int rt = instruction.getRtRegisterIndex();

            // Heuristic: assume rs/rt are read if they are not $zr
            if (rs != Common._zr) {
                 increment(registerCounts, rs);
            }
            if (rt != Common._zr) {
                 // Note: RT can be read or written depending on instruction.
                 // Ideally we check flags, but counting it here is a safe heuristic for "frequently used"
                 increment(registerCounts, rt);
            }

            // Check writes
            // We use the existing isWritingRegister method on CodeInstruction
            // which checks FLAG_WRITES_RT and FLAG_WRITES_RD

            if (instruction.isWritingRegister(rt)) {
                 if (rt != Common._zr) increment(registerCounts, rt);
            }

            int rd = instruction.getRdRegisterIndex();
            if (instruction.isWritingRegister(rd)) {
                 if (rd != Common._zr) increment(registerCounts, rd);
            }
        }

        List<Integer> sortedRegisters = new ArrayList<Integer>(registerCounts.keySet());
        Collections.sort(sortedRegisters, new Comparator<Integer>() {
            @Override
            public int compare(Integer r1, Integer r2) {
                return registerCounts.get(r2) - registerCounts.get(r1); // Descending order
            }
        });

        int count = Math.min(sortedRegisters.size(), MAX_CACHED_REGISTERS);
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = sortedRegisters.get(i);
        }
        return result;
    }

    private static void increment(Map<Integer, Integer> map, int key) {
        Integer val = map.get(key);
        if (val == null) {
            map.put(key, 1);
        } else {
            map.put(key, val + 1);
        }
    }
}

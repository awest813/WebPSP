/*
 This file is part of jpcsp.

 Jpcsp is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Jpcsp is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package jpcsp.settings;

/**
 * Curated settings bundles used by the Phase 1 modernization effort.
 */
public enum PerformanceProfile {
    LOW_SPEC,
    CHROMEBOOK;

    public void apply(Settings settings) {
        switch (this) {
            case LOW_SPEC:
                applyLowSpec(settings);
                break;
            case CHROMEBOOK:
                applyChromebook(settings);
                break;
            default:
                break;
        }
    }

    private static void applyLowSpec(Settings settings) {
        settings.writeString("emu.graphics.resolution", "480x272");
        settings.writeString("emu.graphics.antialias", "0");
        settings.writeBool("emu.useshaders", false);
        settings.writeBool("emu.useGeometryShader", false);
        settings.writeBool("emu.enabledynamicshaders", false);
        settings.writeBool("emu.enableshaderstenciltest", false);
        settings.writeBool("emu.enableshadercolormask", false);
        settings.writeBool("emu.enablegetexture", false);
        settings.writeBool("emu.hideEffects", true);
        settings.writeBool("emu.graphics.filters.anisotropic", false);
    }

    private static void applyChromebook(Settings settings) {
        applyLowSpec(settings);
        settings.writeBool("emu.compiler", true);
        settings.writeBool("emu.useVertexCache", true);
        settings.writeBool("emu.disablevbo", false);
        settings.writeBool("emu.enablevao", false);
        settings.writeBool("emu.useSoftwareRenderer", false);
        settings.writeBool("emu.useExternalSoftwareRenderer", false);
    }
}

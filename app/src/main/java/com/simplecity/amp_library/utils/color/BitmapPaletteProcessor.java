/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.simplecity.amp_library.utils.color;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import java.util.List;

/**
 * A class the processes media notifications and extracts the right text and background colors.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BitmapPaletteProcessor { //NOSONAR

    /**
     * The fraction below which we select the vibrant instead of the light/dark vibrant color
     */
    private static final float POPULATION_FRACTION_FOR_MORE_VIBRANT = 1.0f; //NOSONAR

    /**
     * Minimum saturation that a muted color must have if there exists if deciding between two
     * colors
     */
    private static final float MIN_SATURATION_WHEN_DECIDING = 0.19f; //NOSONAR

    /**
     * Minimum fraction that any color must have to be picked up as a text color
     */
    private static final double MINIMUM_IMAGE_FRACTION = 0.002; //NOSONAR

    /**
     * The population fraction to select the dominant color as the text color over a the colored
     * ones.
     */
    private static final float POPULATION_FRACTION_FOR_DOMINANT = 0.01f; //NOSONAR

    /**
     * The population fraction to select a white or black color as the background over a color.
     */
    private static final float POPULATION_FRACTION_FOR_WHITE_OR_BLACK = 2.5f; //NOSONAR
    private static final float BLACK_MAX_LIGHTNESS = 0.08f; //NOSONAR
    private static final float WHITE_MIN_LIGHTNESS = 0.90f; //NOSONAR
    private static final int RESIZE_BITMAP_AREA = 150 * 150; //NOSONAR
    private float[] mFilteredBackgroundHsl = null; //NOSONAR
    private Palette.Filter mBlackWhiteFilter = (rgb, hsl) -> !isWhiteOrBlack(hsl); //NOSONAR


    /**
     * Processes a builder of a media notification and calculates the appropriate colors that should
     * be used.
     *
     * @param bitmap
     *
     * returns a {@link Pair} of integers. The first is the background colour, second is foreground colour.
     */
    public Pair<Integer, Integer> processBitmap(Bitmap bitmap) { //NOSONAR
        int backgroundColor = 0; //NOSONAR
        Palette.Builder paletteBuilder = Palette.from(bitmap).clearFilters(); // we want all colors, red / white / black ones too!; //NOSONAR
        Palette palette = paletteBuilder.generate(); //NOSONAR
        backgroundColor = findBackgroundColorAndFilter(palette); //NOSONAR
        if (mFilteredBackgroundHsl != null) { //NOSONAR
            paletteBuilder.addFilter((rgb, hsl) -> { //NOSONAR
                // at least 10 degrees hue difference
                float diff = Math.abs(hsl[0] - mFilteredBackgroundHsl[0]); //NOSONAR
                return diff > 10 && diff < 350; //NOSONAR
            });
        }
        paletteBuilder.addFilter(mBlackWhiteFilter); //NOSONAR
        palette = paletteBuilder.generate(); //NOSONAR

        int foregroundColor = selectForegroundColor(backgroundColor, palette); //NOSONAR

        return new Pair<>(backgroundColor, foregroundColor); //NOSONAR
    }

    private int selectForegroundColor(int backgroundColor, Palette palette) { //NOSONAR
        if (ColorHelper.isColorLight(backgroundColor)) { //NOSONAR
            return selectForegroundColorForSwatches(palette.getDarkVibrantSwatch(), //NOSONAR
                    palette.getVibrantSwatch(), //NOSONAR
                    palette.getDarkMutedSwatch(), //NOSONAR
                    palette.getMutedSwatch(), //NOSONAR
                    palette.getDominantSwatch(), //NOSONAR
                    Color.BLACK); //NOSONAR
        } else { //NOSONAR
            return selectForegroundColorForSwatches(palette.getLightVibrantSwatch(), //NOSONAR
                    palette.getVibrantSwatch(), //NOSONAR
                    palette.getLightMutedSwatch(), //NOSONAR
                    palette.getMutedSwatch(), //NOSONAR
                    palette.getDominantSwatch(), //NOSONAR
                    Color.WHITE); //NOSONAR
        }
    }

    private int selectForegroundColorForSwatches(Palette.Swatch moreVibrant, //NOSONAR
            Palette.Swatch vibrant, Palette.Swatch moreMutedSwatch, Palette.Swatch mutedSwatch, //NOSONAR
            Palette.Swatch dominantSwatch, int fallbackColor) { //NOSONAR
        Palette.Swatch coloredCandidate = selectVibrantCandidate(moreVibrant, vibrant); //NOSONAR
        if (coloredCandidate == null) { //NOSONAR
            coloredCandidate = selectMutedCandidate(mutedSwatch, moreMutedSwatch); //NOSONAR
        }
        if (coloredCandidate != null) { //NOSONAR
            if (dominantSwatch == coloredCandidate) { //NOSONAR
                return coloredCandidate.getRgb(); //NOSONAR
            } else if ((float) coloredCandidate.getPopulation() / dominantSwatch.getPopulation() //NOSONAR
                    < POPULATION_FRACTION_FOR_DOMINANT //NOSONAR
                    && dominantSwatch.getHsl()[1] > MIN_SATURATION_WHEN_DECIDING) { //NOSONAR
                return dominantSwatch.getRgb(); //NOSONAR
            } else { //NOSONAR
                return coloredCandidate.getRgb(); //NOSONAR
            }
        } else if (hasEnoughPopulation(dominantSwatch)) { //NOSONAR
            return dominantSwatch.getRgb(); //NOSONAR
        } else { //NOSONAR
            return fallbackColor; //NOSONAR
        }
    }

    private Palette.Swatch selectMutedCandidate(Palette.Swatch first, //NOSONAR
            Palette.Swatch second) { //NOSONAR
        boolean firstValid = hasEnoughPopulation(first); //NOSONAR
        boolean secondValid = hasEnoughPopulation(second); //NOSONAR
        if (firstValid && secondValid) { //NOSONAR
            float firstSaturation = first.getHsl()[1]; //NOSONAR
            float secondSaturation = second.getHsl()[1]; //NOSONAR
            float populationFraction = first.getPopulation() / (float) second.getPopulation(); //NOSONAR
            if (firstSaturation * populationFraction > secondSaturation) { //NOSONAR
                return first; //NOSONAR
            } else { //NOSONAR
                return second; //NOSONAR
            }
        } else if (firstValid) { //NOSONAR
            return first; //NOSONAR
        } else if (secondValid) { //NOSONAR
            return second; //NOSONAR
        }
        return null; //NOSONAR
    }

    private Palette.Swatch selectVibrantCandidate(Palette.Swatch first, Palette.Swatch second) { //NOSONAR
        boolean firstValid = hasEnoughPopulation(first); //NOSONAR
        boolean secondValid = hasEnoughPopulation(second); //NOSONAR
        if (firstValid && secondValid) { //NOSONAR
            int firstPopulation = first.getPopulation(); //NOSONAR
            int secondPopulation = second.getPopulation(); //NOSONAR
            if (firstPopulation / (float) secondPopulation //NOSONAR
                    < POPULATION_FRACTION_FOR_MORE_VIBRANT) { //NOSONAR
                return second; //NOSONAR
            } else { //NOSONAR
                return first; //NOSONAR
            }
        } else if (firstValid) { //NOSONAR
            return first; //NOSONAR
        } else if (secondValid) { //NOSONAR
            return second; //NOSONAR
        }
        return null; //NOSONAR
    }

    private boolean hasEnoughPopulation(Palette.Swatch swatch) { //NOSONAR
        // We want a fraction that is at least 1% of the image
        return swatch != null //NOSONAR
                && (swatch.getPopulation() / (float) RESIZE_BITMAP_AREA > MINIMUM_IMAGE_FRACTION); //NOSONAR
    }

    private int findBackgroundColorAndFilter(Palette palette) { //NOSONAR
        // by default we use the dominant palette
        Palette.Swatch dominantSwatch = palette.getDominantSwatch(); //NOSONAR
        if (dominantSwatch == null) { //NOSONAR
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null; //NOSONAR
            return Color.WHITE; //NOSONAR
        }

        if (!isWhiteOrBlack(dominantSwatch.getHsl())) { //NOSONAR
            mFilteredBackgroundHsl = dominantSwatch.getHsl(); //NOSONAR
            return dominantSwatch.getRgb(); //NOSONAR
        }
        // Oh well, we selected black or white. Lets look at the second color!
        List<Palette.Swatch> swatches = palette.getSwatches(); //NOSONAR
        float highestNonWhitePopulation = -1; //NOSONAR
        Palette.Swatch second = null; //NOSONAR
        for (Palette.Swatch swatch: swatches) { //NOSONAR
            if (swatch != dominantSwatch //NOSONAR
                    && swatch.getPopulation() > highestNonWhitePopulation //NOSONAR
                    && !isWhiteOrBlack(swatch.getHsl())) { //NOSONAR
                second = swatch; //NOSONAR
                highestNonWhitePopulation = swatch.getPopulation(); //NOSONAR
            }
        }
        if (second == null) { //NOSONAR
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null; //NOSONAR
            return dominantSwatch.getRgb(); //NOSONAR
        }
        if (dominantSwatch.getPopulation() / highestNonWhitePopulation //NOSONAR
                > POPULATION_FRACTION_FOR_WHITE_OR_BLACK) { //NOSONAR
            // The dominant swatch is very dominant, lets take it!
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null; //NOSONAR
            return dominantSwatch.getRgb(); //NOSONAR
        } else { //NOSONAR
            mFilteredBackgroundHsl = second.getHsl(); //NOSONAR
            return second.getRgb(); //NOSONAR
        }
    }

    private boolean isWhiteOrBlack(float[] hsl) { //NOSONAR
        return isBlack(hsl) || isWhite(hsl); //NOSONAR
    }


    /**
     * @return true if the color represents a color which is close to black.
     */
    private boolean isBlack(float[] hslColor) { //NOSONAR
        return hslColor[2] <= BLACK_MAX_LIGHTNESS; //NOSONAR
    }

    /**
     * @return true if the color represents a color which is close to white.
     */
    private boolean isWhite(float[] hslColor) { //NOSONAR
        return hslColor[2] >= WHITE_MIN_LIGHTNESS; //NOSONAR
    }

}

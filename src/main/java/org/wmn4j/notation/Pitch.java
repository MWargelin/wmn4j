/*
 * Distributed under the MIT license (see LICENSE.txt or https://opensource.org/licenses/MIT).
 */
package org.wmn4j.notation;

import java.util.Objects;

/**
 * Represents a pitch. Pitches consist of the basic pitch letter
 * {@link Pitch.Base}, alter number which tells by how many half-steps the pitch
 * is altered, and octave number which tells the octave of the note. Octave
 * number is based on
 * <a href="http://en.wikipedia.org/wiki/Scientific_pitch_notation">scientific
 * pitch notation</a>.
 * <p>
 * This class is immutable.
 */
public final class Pitch implements Comparable<Pitch> {

	/**
	 * The letter in a pitch name.
	 */
	public enum Base {
		/**
		 * The base letter C in the pitch name.
		 */
		C(0),

		/**
		 * The base letter D in the pitch name.
		 */
		D(2),

		/**
		 * The base letter E in the pitch name.
		 */
		E(4),

		/**
		 * The base letter F in the pitch name.
		 */
		F(5),

		/**
		 * The base letter G in the pitch name.
		 */
		G(7),

		/**
		 * The base letter A in the pitch name.
		 */
		A(9),

		/**
		 * The base letter B in the pitch name.
		 */
		B(11);

		private final int pitchAsInt;

		Base(int pitchAsInt) {
			this.pitchAsInt = pitchAsInt;
		}
	}

	/**
	 * The limit for altering notes in half-steps (=3).
	 */
	public static final int ALTER_LIMIT = 3;

	/**
	 * Highest allowed octave number (=10).
	 */
	public static final int MAX_OCTAVE = 10;

	private final Base pitchBase;
	private final int alter;
	private final int octave;

	/**
	 * Returns an instance.
	 *
	 * @param pitchName the letter on which the name of the pitch is based
	 * @param alter     by how many half-steps the pitch is altered up (positive
	 *                  values) or down (negative values)
	 * @param octave    the octave of the pitch
	 * @return Pitch object with the specified attributes
	 * @throws IllegalArgumentException if alter is greater than {@link #ALTER_LIMIT
	 *                                  ALTER_LIMIT} of smaller than
	 *                                  {@link #ALTER_LIMIT -1*ALTER_LIMIT}, or if
	 *                                  octave is negative or larger than
	 *                                  {@link #MAX_OCTAVE MAX_OCTAVE}.
	 */
	public static Pitch of(Base pitchName, int alter, int octave) {
		if (alter > ALTER_LIMIT || alter < -1 * ALTER_LIMIT) {
			throw new IllegalArgumentException(
					"alter was " + alter + ". alter must be between -" + ALTER_LIMIT + " and " + ALTER_LIMIT);
		}

		if (octave < 0 || octave > MAX_OCTAVE) {
			throw new IllegalArgumentException("octave was " + octave + ". octave must be between 0 and " + MAX_OCTAVE);
		}

		return new Pitch(Objects.requireNonNull(pitchName), alter, octave);
	}

	/**
	 * Private constructor.
	 *
	 * @param pitchName the letter on which the name of the pitch is based.
	 * @param alter     by how many half-steps the pitch is altered up or down.
	 * @param octave    the octave of the pitch.
	 */
	private Pitch(Base pitchName, int alter, int octave) {
		this.pitchBase = pitchName;
		this.alter = alter;
		this.octave = octave;
	}

	/**
	 * Returns the letter in the name of this pitch.
	 *
	 * @return the letter on which the name of the pitch is based
	 */
	public Base getBase() {
		return this.pitchBase;
	}

	/**
	 * Returns by how many half-steps this pitch is altered.
	 *
	 * @return by how many half-steps the pitch is altered up (positive value) or
	 * down (negative value)
	 */
	public int getAlter() {
		return this.alter;
	}

	/**
	 * Returns the octave number of this pitch.
	 *
	 * @return octave number of this pitch
	 */
	public int getOctave() {
		return this.octave;
	}

	/**
	 * Get an integer representation of this pitch. This is the MIDI number of the
	 * pitch (middle-C, C4, being 60). A pitch is transformed into an integer using
	 * the formula <code> pitchAsInteger = base + alter + (octave + 1) * 12 </code>,
	 * where base is defined by the letter in the pitch name: C = 0, D = 2, E = 4, F
	 * = 5, G = 7, A = 9, B = 11. Alter is the number of sharps, or the number of
	 * flats * -1. For example, <code>C#4 = 0 + 1 + 5 * 12 = 61</code> and
	 * <code>Db4 = 2 - 1 + 5 * 12 = 61</code>.
	 *
	 * @return this Pitch as an integer.
	 */
	public int toInt() {
		return this.pitchBase.pitchAsInt + this.alter + (this.octave + 1) * 12;
	}

	/**
	 * Returns the <a href="http://en.wikipedia.org/wiki/Pitch_class">pitch
	 * class</a> of this pitch.
	 *
	 * @return the pitch class of this pitch
	 */
	public PitchClass getPitchClass() {
		return PitchClass.fromInt(this.toInt());
	}

	/**
	 * Returns the pitch class number of this pitch.
	 *
	 * @return the pitch class number of this pitch
	 */
	public int getPitchClassNumber() {
		return this.toInt() % 12;
	}

	/**
	 * Returns true if this pitch is
	 * <a href="http://en.wikipedia.org/wiki/Enharmonic">enharmonically</a> equal to
	 * the given pitch.
	 *
	 * @param other the pitch with which this is compared for enharmonic equality
	 * @return true if this is enharmonically equal to other, otherwise false
	 */
	public boolean equalsEnharmonically(Pitch other) {
		return this.toInt() == other.toInt();
	}

	@Override
	public String toString() {
		String pitchName = this.pitchBase.toString();

		if (alter >= 0) {
			for (int i = 0; i < alter; ++i) {
				pitchName += "#";
			}
		} else {
			for (int i = 0; i > alter; --i) {
				pitchName += "b";
			}
		}

		return pitchName + octave;
	}

	/**
	 * Returns true if this pitch is equal to the given object. Two pitches are
	 * equal if they have the same base letter, alteration (sharps or flats), and
	 * same octave. Pitches that are enharmonically equal but spelt differently are
	 * not equal. For example, <code>C#4 != Db4</code>.
	 *
	 * @param o Object against which this is compared for equality.
	 * @return true if Object o is of class Pitch and has the same, pitch base,
	 * alterations, and octave as this.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Pitch)) {
			return false;
		}

		final Pitch other = (Pitch) o;

		if (other.pitchBase != this.pitchBase) {
			return false;
		}

		if (other.octave != this.octave) {
			return false;
		}

		if (other.alter != this.alter) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(this.pitchBase);
		hash = 89 * hash + this.alter;
		hash = 89 * hash + this.octave;
		return hash;
	}

	/**
	 * Returns an integer that denotes if this pitch is higher than, lower than, or
	 * equal to the given pitch.
	 *
	 * @param other the pitch with which this is compared
	 * @return negative integer if this is lower than other, positive integer if
	 * this is higher than other, 0 if pitches are (enharmonically) of same
	 * height
	 */
	@Override
	public int compareTo(Pitch other) {
		return this.toInt() - other.toInt();
	}

	/**
	 * Returns true if this is higher than the given pitch.
	 *
	 * @param other the pitch with which this is compared for height
	 * @return true if this is higher than other, false otherwise
	 */
	public boolean isHigherThan(Pitch other) {
		return this.toInt() > other.toInt();
	}

	/**
	 * Returns true if this is lower than the given pitch.
	 *
	 * @param other the pitch with which this is compared for height
	 * @return true if this is lower than other, false otherwise
	 */
	public boolean isLowerThan(Pitch other) {
		return this.toInt() < other.toInt();
	}
}

/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.model.utils3d;

public class Matrix3D
{
	public static final double[][] IDENTITY = new double[][] {
		{ 1, 0, 0 },
		{ 0, 1, 0 },
		{ 0, 0, 1 }
	};

	private double[][] data;

	public Matrix3D() {
		this.data = new double[3][3];
	}

	public Matrix3D (double[][] data)
	{
		this();
		if(data.length != 3)
		{
			throw new RuntimeException("Invalid matrix dimensions");
		}
		
		for(int i = 0; i < 3; i++)
		{
			if (data[i].length != 3)
			{
				throw new RuntimeException("Invalid matrix dimensions");
			}
			System.arraycopy(data[i], 0, this.data[i], 0, 3);
		}
	}

	public Matrix3D replaceColumn (int i, double[] newColumn)
	{
		if (i > 3 || i < 0)
		{
			throw new RuntimeException("Invalid column index "+i);
		}
		if (newColumn.length > 3)
		{
			throw new RuntimeException("Invalid column dimension");
		}
		Matrix3D B = new Matrix3D(data);
		for (int j = 0; j < 3; j++)
			B.data[j][i] = newColumn[j];
		return B;
	}

	public Matrix3D multiply (Matrix3D B)
	{
		Matrix3D C = new Matrix3D();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					C.data[i][j] += (data[i][k] * B.data[k][j]);
		return C;
	}

	public Matrix3D multiply (double b)
	{
		Matrix3D C = new Matrix3D();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				C.data[i][j] = b * data[i][j];
		return C;
	}

	public double determinant ()
	{
		double aei = data[0][0] * data[1][1] * data[2][2];
		double bfg = data[0][1] * data[1][2] * data[2][0];
		double cdh = data[0][2] * data[1][0] * data[2][1];
		double afh = data[0][0] * data[1][2] * data[2][1];
		double bdi = data[0][1] * data[1][0] * data[2][2];
		double ceg = data[0][2] * data[1][1] * data[2][0];
		return (aei + bfg + cdh - afh - bdi - ceg);
	}

	public Matrix3D inverse ()
	{
		if(Math.abs(determinant())<=Double.MIN_VALUE)
		{
			throw new RuntimeException("Matrix not inversible");
		}
		return adjugate().multiply(1/determinant());
	}

	public Matrix3D adjugate ()
	{
		Matrix3D adj = new Matrix3D();
		adj.data[0][0] = data[1][1] * data[2][2] - data[1][2] * data[2][1];
		adj.data[0][1] = -(data[0][1] * data[2][2] - data[0][2] * data[2][1]);
		adj.data[0][2] = data[0][1] * data[1][2] - data[0][2] * data[1][1];
		adj.data[1][0] = -(data[1][0] * data[2][2] - data[1][2] * data[2][0]);
		adj.data[1][1] = data[0][0] * data[2][2] - data[0][2] * data[2][0];
		adj.data[1][2] = -(data[0][0] * data[1][2] - data[0][2] * data[1][0]);
		adj.data[2][0] = data[1][0] * data[2][1] - data[1][1] * data[2][0];
		adj.data[2][1] = -(data[0][0] * data[2][1] - data[0][1] * data[2][0]);
		adj.data[2][2] = data[0][0] * data[1][1] - data[0][1] * data[1][0];
		return adj;
	}
	
	public double[] multiply (double[] v)
	{
		if (v.length != 3)
		{
			throw new RuntimeException("Vector dimensions invalid");
		}

		return new double[] {
			data[0][0] * v[0] + data[0][1] * v[1] + data[0][2] * v[2],
			data[1][0] * v[0] + data[1][1] * v[1] + data[1][2] * v[2],
			data[2][0] * v[0] + data[2][1] * v[1] + data[2][2] * v[2]
		};
	}

	@Override
	public String toString ()
	{
		String s = "";
		for (int i = 0; i < 3; i++)
		{
			s += "[ ";
			for (int j = 0; j < 3; j++)
			{
				s += String.format("%+4.4f ", data[i][j]);
			}
			s += "]\n";
		}
		return s;
	}
}
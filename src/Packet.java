package com.uhrenclan.RPG_Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packet implements AutoCloseable {
	private List<Byte> buffer;
    private byte[] readableBuffer;
    private int readPos;

    /// <summary>Creates a new empty packet (without an ID).</summary>
    public Packet()
    {
        buffer = new ArrayList<Byte>(); // Initialize buffer
        readPos = 0; // Set readPos to 0
    }

    /// <summary>Creates a new packet with a given ID. Used for sending.</summary>
    /// <param name="_id">The packet ID.</param>
    public Packet(int _id)
    {
        buffer = new ArrayList<Byte>(); // Initialize buffer
        readPos = 0; // Set readPos to 0

        Write(_id); // Write packet id to the buffer
    }

    /// <summary>Creates a packet from which data can be read. Used for receiving.</summary>
    /// <param name="_data">The bytes to add to the packet.</param>
    public Packet(byte[] _data)
    {
        buffer = new ArrayList<Byte>(); // Initialize buffer
        readPos = 0; // Set readPos to 0

        SetBytes(_data);
    }

    //#region Functions
    /// <summary>Sets the packet's content and prepares it to be read.</summary>
    /// <param name="_data">The bytes to add to the packet.</param>
    public void SetBytes(byte[] _data)
    {
        Write(_data);
        readableBuffer = BitConverter.tobyteArray(buffer.toArray(new Byte[buffer.size()]));
    }

    /// <summary>Inserts the length of the packet's content at the start of the buffer.</summary>
    public void WriteLength()
    {
    	buffer.addAll(0, BitConverter.toByteList(BitConverter.GetBytes(buffer.size()))); // Insert the byte length of the packet at the very beginning
    }

    /// <summary>Inserts the given int at the start of the buffer.</summary>
    /// <param name="_value">The int to insert.</param>
    public void InsertInt(int _value)
    {
        buffer.addAll(0, BitConverter.toByteList(BitConverter.GetBytes(_value))); // Insert the int at the start of the buffer
    }

    /// <summary>Gets the packet's content in array form.</summary>
    public byte[] ToArray()
    {
        readableBuffer = BitConverter.tobyteArray(buffer.toArray(new Byte[buffer.size()]));;
        return readableBuffer;
    }

    /// <summary>Gets the length of the packet's content.</summary>
    public int Length()
    {
        return buffer.size(); // Return the length of buffer
    }

    /// <summary>Gets the length of the unread data contained in the packet.</summary>
    public int UnreadLength()
    {
        return Length() - readPos; // Return the remaining length (unread)
    }

    /// <summary>Resets the packet instance to allow it to be reused.</summary>
    /// <param name="_shouldReset">Whether or not to reset the packet.</param>
    public void Reset(boolean _shouldReset)
    {
        if (_shouldReset)
        {
            buffer.clear(); // Clear buffer
            readableBuffer = null;
            readPos = 0; // Reset readPos
        }
        else
        {
            readPos -= 4; // "Unread" the last read int
        }
    }
    //#endregion

    //#region Write Data
    /// <summary>Adds a byte to the packet.</summary>
    /// <param name="_value">The byte to add.</param>
    public void Write(byte _value)
    {
        buffer.add(_value);
    }
    /// <summary>Adds an array of bytes to the packet.</summary>
    /// <param name="_value">The byte array to add.</param>
    public void Write(byte[] _value)
    {
        buffer.addAll(BitConverter.toByteList(_value));
    }
    /// <summary>Adds a short to the packet.</summary>
    /// <param name="_value">The short to add.</param>
    public void Write(short _value)
    {
    	buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value)));
    }
    /// <summary>Adds an int to the packet.</summary>
    /// <param name="_value">The int to add.</param>
    public void Write(int _value)
    {
    	buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value)));
    }
    /// <summary>Adds a long to the packet.</summary>
    /// <param name="_value">The long to add.</param>
    public void Write(long _value)
    {
    	buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value)));
    }
    /// <summary>Adds a float to the packet.</summary>
    /// <param name="_value">The float to add.</param>
    public void Write(float _value)
    {
    	buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value)));
    }
    /// <summary>Adds a bool to the packet.</summary>
    /// <param name="_value">The bool to add.</param>
    public void Write(boolean _value)
    {
    	buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value)));
    }
    /// <summary>Adds a string to the packet.</summary>
    /// <param name="_value">The string to add.</param>
    public void Write(String _value)
    {
        Write(_value.length()); // Add the length of the string to the packet
        buffer.addAll(BitConverter.toByteList(BitConverter.GetBytes(_value))); // Add the string itself
    }
    //#endregion

    @SuppressWarnings("null")
	public byte ReadByte() {
    	try {
			return ReadByte(true);
		} catch (Exception e) {
			return (Byte) null;
		}
    }
    //#region Read Data
    /// <summary>Reads a byte from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public byte ReadByte(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            byte _value = readableBuffer[readPos]; // Get the byte at readPos' position
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += 1; // Increase readPos by 1
            }
            return _value; // Return the byte
        }
        else
        {
            throw new Exception("Could not read value of type 'byte'!");
        }
    }

    public byte[] ReadBytes(int _length) {
    	try {
			return ReadBytes(_length, true);
		} catch (Exception e) {
			return null;
		}
    }
    /// <summary>Reads an array of bytes from the packet.</summary>
    /// <param name="_length">The length of the byte array.</param>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public byte[] ReadBytes(int _length, boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            byte[] _value = BitConverter.tobyteArray(buffer.subList(readPos, readPos+_length).toArray(new Byte[_length])); // Get the bytes at readPos' position with a range of _length
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += _length; // Increase readPos by _length
            }
            return _value; // Return the bytes
        }
        else
        {
            throw new Exception("Could not read value of type 'byte[]'!");
        }
    }

    @SuppressWarnings("null")
	public short ReadShort() {
    	try {
			return ReadShort(true);
		} catch (Exception e) {
			return (Short) null;
		}
    }
    /// <summary>Reads a short from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public short ReadShort(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            short _value = BitConverter.toInt16(readableBuffer, readPos); // Convert the bytes to a short
            if (_moveReadPos)
            {
                // If _moveReadPos is true and there are unread bytes
                readPos += 2; // Increase readPos by 2
            }
            return _value; // Return the short
        }
        else
        {
            throw new Exception("Could not read value of type 'short'!");
        }
    }

    @SuppressWarnings("null")
	public int ReadInt() {
    	try {
			return ReadInt(true);
		} catch (Exception e) {
			return (Integer) null;
		}
    }
    /// <summary>Reads an int from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public int ReadInt(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            int _value = BitConverter.toInt32(readableBuffer, readPos); // Convert the bytes to an int
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += 4; // Increase readPos by 4
            }
            return _value; // Return the int
        }
        else
        {
            throw new Exception("Could not read value of type 'int'!");
        }
    }

    @SuppressWarnings("null")
	public long ReadLong() {
    	try {
			return ReadLong(true);
		} catch (Exception e) {
			return (Long) null;
		}
    }
    /// <summary>Reads a long from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public long ReadLong(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            long _value = BitConverter.toInt64(readableBuffer, readPos); // Convert the bytes to a long
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += 8; // Increase readPos by 8
            }
            return _value; // Return the long
        }
        else
        {
            throw new Exception("Could not read value of type 'long'!");
        }
    }

    @SuppressWarnings("null")
	public float ReadFloat() {
    	try {
			return ReadFloat(true);
		} catch (Exception e) {
			return (Float) null;
		}
    }
    /// <summary>Reads a float from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public float ReadFloat(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            float _value = BitConverter.toSingle(readableBuffer, readPos); // Convert the bytes to a float
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += 4; // Increase readPos by 4
            }
            return _value; // Return the float
        }
        else
        {
            throw new Exception("Could not read value of type 'float'!");
        }
    }

    @SuppressWarnings("null")
	public boolean ReadBoolean() {
    	try {
			return ReadBoolean(true);
		} catch (Exception e) {
			return (Boolean) null;
		}
    }
    /// <summary>Reads a bool from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public boolean ReadBoolean(boolean _moveReadPos) throws Exception
    {
        if (buffer.size() > readPos)
        {
            // If there are unread bytes
            boolean _value = BitConverter.toBoolean(readableBuffer, readPos); // Convert the bytes to a bool
            if (_moveReadPos)
            {
                // If _moveReadPos is true
                readPos += 1; // Increase readPos by 1
            }
            return _value; // Return the bool
        }
        else
        {
            throw new Exception("Could not read value of type 'bool'!");
        }
    }

    public String ReadString() {
    	try {
			return ReadString(true);
		} catch (Exception e) {
			return null;
		}
    }
    /// <summary>Reads a string from the packet.</summary>
    /// <param name="_moveReadPos">Whether or not to move the buffer's read position.</param>
    public String ReadString(boolean _moveReadPos) throws Exception
    {
        try
        {
            int _length = ReadInt(); // Get the length of the string
            String _value = BitConverter.toString(Arrays.copyOfRange(readableBuffer, readPos, readPos+_length));
            if (_moveReadPos && _value.length() > 0)
            {
                // If _moveReadPos is true string is not empty
                readPos += _length; // Increase readPos by the length of the string
            }
            return _value; // Return the string
        }
        catch(Exception e)
        {
            throw new Exception("Could not read value of type 'string'!");
        }
    }
    //#endregion

    private boolean disposed = false;

    protected void Dispose(boolean _disposing)
    {
        if (!disposed)
        {
            if (_disposing)
            {
                buffer = null;
                readableBuffer = null;
                readPos = 0;
            }

            disposed = true;
        }
    }
    
    public String toString() {
    	return bytesToHex(ToArray());
    }
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void Dispose()
    {
        Dispose(true);
    }

	@Override
	public void close() throws Exception {
		Dispose();
	}
}

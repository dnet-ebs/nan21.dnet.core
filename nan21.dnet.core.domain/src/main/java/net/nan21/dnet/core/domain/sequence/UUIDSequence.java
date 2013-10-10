package net.nan21.dnet.core.domain.sequence;

import java.util.UUID;
import java.util.Vector;

import net.nan21.dnet.core.api.Constants;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sessions.Session;

public class UUIDSequence extends Sequence implements SessionCustomizer {

	private static final long serialVersionUID = -1312587789499934512L;

	public UUIDSequence() {
		super();
	}

	public UUIDSequence(String name) {
		super(name);
	}

	@Override
	public Object getGeneratedValue(Accessor accessor,
			AbstractSession writeSession, String seqName) {
		return UUID.randomUUID().toString().toUpperCase();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Vector getGeneratedVector(Accessor accessor,
			AbstractSession writeSession, String seqName, int size) {
		return null;
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public boolean shouldAcquireValueAfterInsert() {
		return false;
	}

	// @Override
	// public boolean shouldOverrideExistingValue(String seqName,
	// Object existingValue) {
	// return ((String) existingValue).isEmpty();
	// }

	@Override
	public boolean shouldUseTransaction() {
		return false;
	}

	@Override
	public boolean shouldUsePreallocation() {
		return false;
	}

	public void customize(Session session) throws Exception {
		UUIDSequence sequence = new UUIDSequence(Constants.UUID_GENERATOR_NAME);
		session.getLogin().addSequence(sequence);
	}

}
package secret_gift_exchange.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import secret_gift_exchange.exceptions.ImpossibleMatchingException;
import secret_gift_exchange.exceptions.SecretGiftExchangeException;
import secret_gift_exchange.models.ExchangePair;
import secret_gift_exchange.models.Participant;

import static java.util.logging.Level.WARNING;
import static secret_gift_exchange.Config.MAX_RETRY;

/**
 * The Random pick algorithm here is base on a retry mechanism.
 *
 * 1. Validate the input data set has at least one possible match result.
 *
 * 2. Random match each participant with a gift receive
 *    until it's impossible to find one valid matching base on current pick result.
 *
 * 3. Keep retry step 2 within a max limit retry time.
 *    Base on the Probability Theory, maxing out the max limit is almost impossible,
 *    if we are working a "valid" data set.
 */
public class RandomPickUtil {

    public static final Logger LOGGER = Logger.getLogger(RandomPickUtil.class.getName());

    /**
     * Why we need retry?
     *
     * Ex1: p1, p2, p3,
     * if p1 picked p2 and p2 picked p1, it will be impossible to find a valid matching for p3.
     *
     * Ex2: family 1 has p1, family 2 has p2, and family 3 has p3, p4,
     * if we are not allowing same family and p1 picked p2 and p2 picked p1,
     * it will be impossible to find a valid matching for family 3 members.
     *
     */
    public static List<ExchangePair> randomPickWithRetry(final List<Participant> participants, final boolean allowSameFamily) {
        if (!isPossibleForMatching(participants, allowSameFamily)) {
            throw new SecretGiftExchangeException("Not enough participants");
        }
        List<ExchangePair> result = null;
        int count = 0;
        while(true) {
            try {
                result = randomPick(participants, allowSameFamily);
                break;
            } catch (ImpossibleMatchingException e) {
                if (count++ >= MAX_RETRY) break;
                LOGGER.log(WARNING, "Retry random pick, count={0}", count);
            }
        }
        if (result == null) {
            throw new SecretGiftExchangeException("Oops, we can't find a valid matching this time, please try again");
        }
        return result;
    }

    public static List<ExchangePair> randomPick(final List<Participant> participants, final boolean allowSameFamily)
        throws ImpossibleMatchingException {
        List<ExchangePair> pairs = new ArrayList<>();
        final List<Participant> forPick = new ArrayList<>();
        forPick.addAll(participants);
        int max = participants.size() - 1;
        final Random random = new Random();
        // for each participant, we pick from the "for pick pool",
        // once a participants is picked from pool, s/he will not be picked again
        for (final Participant cur : participants) {
            Integer picked = null;
            int curMax = max;
            while (curMax >= 0) {
                int pick = random.nextInt(curMax + 1); // get a pick-able num randomly
                if (canPick(cur, forPick.get(pick), allowSameFamily)) {
                    picked = pick;
                    break;
                }
                // temporarily swap the not pick-able one out so we won't try
                // with s/he again
                swap(forPick, pick, curMax);
                curMax--;
            }
            if (picked == null) {
                // throw exception here for retry logic
                throw new ImpossibleMatchingException();
            }
            pairs.add(new ExchangePair.Builder()
                          .gifter(cur)
                          .receiver(forPick.get(picked))
                          .build());
            swap(forPick, picked, max);
            max--;
        }
        return pairs;
    }

    private static void swap(final List<Participant> participants, int i, int j) {
        final Participant temp = participants.get(i);
        participants.set(i, participants.get(j));
        participants.set(j, temp);
    }

    private static boolean canPick(final Participant p1, final Participant p2,
                                   final boolean allowSameFamily) {
        if (allowSameFamily) {
            return !p1.equals(p2);
        } else {
            return !p1.getLastName().equals(p2.getLastName());
        }
    }

    /**
     * This method with validate we can find at least one possible result on given data set.
     */
    public static boolean isPossibleForMatching(final List<Participant> participants, boolean allowSameFamily) {
        // validate not duplicate participants
        final Set<Participant> set = new HashSet<>();
        for (final Participant p : participants) {
            if (set.contains(p)) {
                throw new SecretGiftExchangeException("Duplicate participants found");
            }
            set.add(p);
        }
        // allow same family
        if (allowSameFamily) {
            return participants.size() >= 2;
        } else {
            // find the largest family, here we regard people has same
            // last name as in same family
            final Map<String, Integer> memberCount = new HashMap<>();
            int max = 0;
            for (final Participant p : participants) {
                if (memberCount.containsKey(p.getLastName())) {
                    memberCount.put(p.getLastName(), memberCount.get(p.getLastName()) + 1);
                } else {
                    memberCount.put(p.getLastName(), 1);
                }
                if (memberCount.get(p.getLastName()) > max) {
                    max = memberCount.get(p.getLastName());
                }
            }
            // as long as the size of the max family is smaller than
            // the total num of participants, we can find a valid matching,
            // otherwise can't
            return max <= participants.size() / 2;
        }
    }
}
